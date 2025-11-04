package org.tidepool.sdk.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.tidepool.sdk.AppLifecycleProvider
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * Manages lifecycle-aware periodic data uploads.
 * This class ensures uploads only happen when the app is in foreground.
 */
class LifecycleAwareDataUploadManager(
    private val lifecycleProvider: AppLifecycleProvider,
    private val scope: CoroutineScope
) {
    
    private var uploadJob: Job? = null
    private var isConfigured = false
    private var uploadPeriod: Duration = 5.minutes
    private var initialDelay: Duration = 30.seconds
    private var uploadAction: (suspend () -> Unit)? = null
    
    /**
     * Configures the periodic upload parameters.
     *
     * @param period Time between upload attempts when app is in foreground
     * @param delay Initial delay before first upload attempt
     * @param action Suspend function to execute for each upload
     */
    fun configure(
        period: Duration,
        delay: Duration = 30.seconds,
        action: suspend () -> Unit
    ) {
        this.uploadPeriod = period
        this.initialDelay = delay
        this.uploadAction = action
        this.isConfigured = true
    }
    
    /**
     * Starts lifecycle-aware periodic uploads.
     * Uploads will only occur when the app is in foreground.
     */
    fun start() {
        if (!isConfigured) {
            throw IllegalStateException("LifecycleAwareDataUploadManager must be configured before starting")
        }
        
        stop()
        
        uploadJob = scope.launch {
            // Wait for initial delay
            delay(initialDelay.inWholeMilliseconds)
            
            // Observe foreground state changes
            lifecycleProvider.foregroundStateFlow
                .distinctUntilChanged()
                .collect {
                    if (it) {
                        startForegroundUploads()
                    } else {
                        stop()
                    }
                }
        }
    }
    
    /**
     * Stops the lifecycle-aware uploads.
     */
    fun stop() {
        uploadJob?.cancel()
        uploadJob = null
    }
    
    /**
     * Checks if the upload manager is currently running.
     */
    fun isActive(): Boolean = uploadJob?.isActive == true
    
    private suspend fun startForegroundUploads() {
        val action = uploadAction ?: return
        
        while (scope.isActive && lifecycleProvider.isInForeground()) {
            try {
                action()
            } catch (e: Exception) {
                // Log error but continue uploads
                // In production, you might want to implement exponential backoff
                // or provide error handling callbacks
            }
            
            // Break early if we went to background during the delay
            val delayMillis = uploadPeriod.inWholeMilliseconds
            val checkInterval = 1000L // Check every second
            var remainingDelay = delayMillis
            
            while (remainingDelay > 0 && lifecycleProvider.isInForeground()) {
                val currentDelay = minOf(checkInterval, remainingDelay)
                delay(currentDelay)
                remainingDelay -= currentDelay
            }
            
            // If we went to background during delay, exit the loop
            if (!lifecycleProvider.isInForeground()) {
                break
            }
        }
    }
}