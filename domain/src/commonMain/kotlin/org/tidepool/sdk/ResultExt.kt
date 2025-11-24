package org.tidepool.sdk

fun <R, T> Result<List<T>>.mapList(block: (T) -> R): Result<List<R>> =
    map { list -> list.map { block(it) } }

fun <T> Result<List<T>>.filterList(block: (T) -> Boolean): Result<List<T>> =
    map { list -> list.filter { block(it) } }

fun <T, R : Comparable<R>> Result<List<T>>.sortList(block: (T) -> R): Result<List<T>> =
    map { list -> list.sortedBy { block(it) } }

fun <T, R : Comparable<R>> Result<List<T>>.sortListDescending(block: (T) -> R): Result<List<T>> =
    map { list -> list.sortedByDescending { block(it) } }

suspend fun <T, R> Result<T>.flatMap(
    block: suspend (T) -> Result<R>
) = fold(
    onSuccess = { block(it) },
    onFailure = { Result.failure(it) },
)