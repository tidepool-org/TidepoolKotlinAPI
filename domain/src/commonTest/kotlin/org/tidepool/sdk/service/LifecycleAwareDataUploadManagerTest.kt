package org.tidepool.sdk.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.tidepool.sdk.AppLifecycleProvider
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class LifecycleAwareDataUploadManagerTest {

    private class FakeLifecycle(inForeground: Boolean) : AppLifecycleProvider {
        val state = MutableStateFlow(inForeground)
        override val foregroundStateFlow: Flow<Boolean> = state
        override fun isInForeground(): Boolean = state.value
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
    fun aQuickBackgroundAndForegroundKeepsUploading() = runTest {
        val lifecycle = FakeLifecycle(inForeground = true)
        var uploads = 0
        startManager(lifecycle) { uploads++ }
        advance(30.seconds)

        // Both changes land between two of the loop's once-a-second foreground checks.
        lifecycle.state.value = false
        lifecycle.state.value = true
        advance(5.minutes)

        assertTrue(uploads >= 5, "still uploading every minute, got $uploads")
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
}
