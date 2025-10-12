package org.tidepool.sdk

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.collections.orEmpty
import kotlin.collections.plus

/**
 * A generic pagination utility that handles paginated API requests with automatic state management.
 *
 * This class provides a reactive way to load paginated data using Kotlin coroutines and flows.
 * It manages the pagination state, handles loading requests, and exposes the accumulated items
 * through a Flow for reactive UI updates.
 *
 * @param Key The type used for pagination keys (e.g., String, Int, custom pagination token)
 * @param Item The type of individual items returned by each pagination request
 * @property initialKey The starting key for pagination requests
 * @property onRequest Suspend function that performs the actual API request for a given key
 * @property getNextKey Suspend function that extracts the next pagination key from the response
 * @property onSuccess Suspend function called when a request succeeds
 * @property onFailure Suspend function called when a request fails
 * @property endReached Function that determines if pagination has reached the end
 */
internal class PaginatorImpl<Key, Item>(
    private val initialKey: Key,
    private val onRequest: suspend (nextKey: Key) -> Result<Item>,
    private val getNextKey: suspend (result: Item, currentKey: Key) -> Key,
    onSuccess: suspend (result: Item, isEndReached: Boolean) -> Unit,
    onFailure: suspend (result: Throwable) -> Unit,
    private val endReached: (result: Item, currentKey: Key) -> Boolean,
) : Paginator<Key, Item>(onSuccess, onFailure) {
    
    /** Internal state flow holding the accumulated list of items */
    private val _items = MutableStateFlow<List<Item>?>(null)
    
    /** Current pagination key used for the next request */
    private var currentKey = initialKey
    
    /** Flag to prevent concurrent requests */
    private var requestMutex = Mutex()
    
    /** Flag indicating if pagination has reached the end */
    private var isEndReached = false
    
    /** Flag indicating if the paginator is being reset */
    private var isReseting = false
    
    override val items: Flow<List<Item>> = _items.filterNotNull()
    
    override suspend fun loadNextItems() {
        println("Paginator: loadNextItems() called with key: $currentKey, ${requestMutex.isLocked} $isEndReached")
        if (requestMutex.isLocked || isEndReached) {
            return
        }
        
        requestMutex
            .withLock {
                onRequest(currentKey)
            }.onFailure {
                onLoadPageFailure(it)
            }.onSuccess { item ->
                currentKey = getNextKey(item, currentKey)
                isEndReached = endReached(item, currentKey)
                onLoadPageSuccess(item, isEndReached)
                _items.update { list ->
                    if (isReseting) {
                        listOf(item)
                    } else {
                        list.orEmpty() + item
                    }
                }
                isReseting = false
            }
    }
    
    override fun reset(clearItems: Boolean) {
        if (clearItems) _items.update { emptyList() }
        currentKey = initialKey
        isEndReached = false
        isReseting = true
    }
}
