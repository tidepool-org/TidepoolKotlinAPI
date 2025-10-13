package org.tidepool.sdk

import kotlinx.coroutines.flow.Flow

abstract class Paginator<Key, Item>(
    protected val onLoadPageSuccess: suspend (result: Item, isEndReached: Boolean) -> Unit,
    protected val onLoadPageFailure: suspend (result: Throwable) -> Unit,
) {
    
    /**
     * Flow of accumulated items from all pagination requests.
     * Emits a new list each time new items are loaded.
     * The flow filters out null values and only emits when items are available.
     */
    abstract val items: Flow<List<Item>>
    
    /**
     * Loads the next batch of items from the paginated source.
     *
     * This method:
     * - Checks if a request is already in progress or if the end has been reached
     * - Makes a request using the current pagination key
     * - Updates the current key for the next request
     * - Appends new items to the existing list (or replaces if resetting)
     * - Calls the appropriate success/failure callbacks
     *
     * The method is safe to call multiple times and will ignore concurrent calls.
     */
    abstract suspend fun loadNextItems()
    
    /**
     * Resets the paginator to its initial state.
     *
     * This method:
     * - Resets the current key to the initial key
     * - Clears the end-reached flag
     * - Sets the reset flag so the next load will replace items instead of appending
     * - Optionally clears the current items list immediately
     *
     * Call this method when you need to start pagination from the beginning,
     * such as when implementing pull-to-refresh functionality.
     */
    abstract fun reset(clearItems: Boolean = false)
}
