package org.tidepool.sdk.service

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.tidepool.sdk.AppLifecycleProvider
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class LifecycleAwareDataUploadManagerTest {

    private class FakeLifecycle(inForeground: Boolean) : AppLifecycleProvider {
        val state = MutableStateFlow(inForeground)
        override val foregroundStateFlow: StateFlow<Boolean> = state

        /**
         * When above 0, the app returns to the foreground right after this many checks have
         * answered "not in the foreground", before anything else reads the state.
         */
        var returnAfterBackgroundChecks = 0

        var reads = 0

        override fun isInForeground(): Boolean {
            // Ends a loop that never suspends, which would otherwise hang the test.
            if (++reads > MAX_READS) throw CancellationException("still reading after $MAX_READS checks")
            val inForeground = state.value
            if (!inForeground && returnAfterBackgroundChecks > 0 && --returnAfterBackgroundChecks == 0) {
                state.value = true
            }
            return inForeground
        }

        companion object {
            const val MAX_READS = 10_000
        }
    }

    private fun TestScope.startManager(lifecycle: FakeLifecycle, onUpload: () -> Unit) =
        LifecycleAwareDataUploadManager(lifecycle, backgroundScope).apply {
            configure(period = 1.minutes, delay = 30.seconds) { onUpload() }
            start()
        }

    private fun TestScope.advance(by: Duration) {
        advanceTimeBy(by.inWholeMilliseconds)
        runCurrent()
    }

    @Test
    fun uploadsEveryPeriodInTheForeground() = runTest {
        var uploads = 0
        startManager(FakeLifecycle(inForeground = true)) { uploads++ }

        advance(30.seconds)
        assertEquals(1, uploads)
        advance(2.minutes)
        assertEquals(3, uploads)
    }

    @Test
    fun uploadsResumeAfterReturningFromTheBackground() = runTest {
        val lifecycle = FakeLifecycle(inForeground = true)
        var uploads = 0
        val manager = startManager(lifecycle) { uploads++ }
        advance(30.seconds)
        assertEquals(1, uploads)

        lifecycle.state.value = false
        advance(10.minutes)
        assertEquals(1, uploads, "no uploads while in the background")

        lifecycle.state.value = true
        advance(1.seconds)
        assertEquals(2, uploads, "an upload as soon as the app is back in the foreground")
        advance(1.minutes)
        assertEquals(3, uploads)
        assertTrue(manager.isActive())
    }

    @Test
    fun returningWhileTheLoopWindsDownKeepsUploading() {
        // The app returns at each point while the loop is noticing the background and stopping.
        // Whoever reads the state next sees `true` again, just as before it left.
        for (checks in 1..3) {
            runTest {
                val lifecycle = FakeLifecycle(inForeground = true)
                var uploads = 0
                startManager(lifecycle) { uploads++ }
                advance(30.seconds)
                assertEquals(1, uploads)

                lifecycle.returnAfterBackgroundChecks = checks
                lifecycle.state.value = false
                advance(5.minutes)

                assertTrue(uploads >= 5, "back after $checks check(s): still uploading every minute, got $uploads")
            }
        }
    }

    @Test
    fun startsUploadingOnTheFirstTripToTheForeground() = runTest {
        val lifecycle = FakeLifecycle(inForeground = false)
        var uploads = 0
        startManager(lifecycle) { uploads++ }
        advance(5.minutes)
        assertEquals(0, uploads)

        lifecycle.state.value = true
        advance(1.seconds)
        assertEquals(1, uploads)
    }

    @Test
    fun stopsOnceItsScopeIsCancelledInTheForeground() = runTest {
        val lifecycle = FakeLifecycle(inForeground = true)
        val uploadScope = CoroutineScope(backgroundScope.coroutineContext + Job())
        val manager = LifecycleAwareDataUploadManager(lifecycle, uploadScope).apply {
            configure(period = 1.minutes, delay = 30.seconds) {
                // Cancelled during an upload while the app leaves and comes straight back, so the
                // wind-down reads the background once and then the foreground, without suspending.
                uploadScope.cancel()
                lifecycle.returnAfterBackgroundChecks = 1
                lifecycle.state.value = false
            }
            start()
        }

        advance(30.seconds)

        assertFalse(manager.isActive())
        assertTrue(lifecycle.reads < 100, "stopped after a few checks, got ${lifecycle.reads}")
    }
}
