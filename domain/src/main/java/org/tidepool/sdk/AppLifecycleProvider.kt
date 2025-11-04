package org.tidepool.sdk

import kotlinx.coroutines.flow.Flow

/**
 * Pure Kotlin interface for providing app lifecycle events.
 * This allows the SDK to be platform-agnostic while still supporting lifecycle-aware operations.
 */
interface AppLifecycleProvider {
    
    /**
     * Flow that emits true when app comes to foreground, false when it goes to background.
     */
    val foregroundStateFlow: Flow<Boolean>
    
    /**
     * Current foreground state of the app.
     * @return true if app is in foreground, false otherwise
     */
    fun isInForeground(): Boolean
}

/**
 * Enum representing the different lifecycle states of an application.
 */
enum class AppLifecycleState {
    
    /**
     * Application is in the foreground and visible to the user.
     */
    FOREGROUND,
    
    /**
     * Application is in the background and not visible to the user.
     */
    BACKGROUND,
    
    /**
     * Initial state or unknown state.
     */
    UNKNOWN
}

/**
 * Data class representing a lifecycle state change event.
 */
data class AppLifecycleEvent(
    val previousState: AppLifecycleState,
    val currentState: AppLifecycleState,
    val timestamp: Long = System.currentTimeMillis()
) {
    
    /**
     * Convenience property to check if the app transitioned to foreground.
     */
    val isEnteringForeground: Boolean
        get() = previousState == AppLifecycleState.BACKGROUND && currentState == AppLifecycleState.FOREGROUND
    
    /**
     * Convenience property to check if the app transitioned to background.
     */
    val isEnteringBackground: Boolean
        get() = previousState == AppLifecycleState.FOREGROUND && currentState == AppLifecycleState.BACKGROUND
}