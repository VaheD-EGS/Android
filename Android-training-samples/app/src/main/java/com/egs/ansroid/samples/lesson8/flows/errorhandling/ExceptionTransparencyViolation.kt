package com.egs.ansroid.samples.lesson8.flows.errorhandling

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking

private val upstreamViolation1: Flow<Int> = flow {
    for (i in 1..3) {
        try {
            emit(i)
        } catch (e: Throwable) {
            println("Intercept downstream exception $e")
        }
    }
}

val otherViolation: Flow<Int> = flowOf(1, 2, 3).handleErrors()

fun <T> Flow<T>.handleErrors(): Flow<T> = flow {
    try {
        collect { value -> emit(value) }
    } catch (e: Throwable) {
        Log.e(TAG,"error")
    }
}

fun innerExcViolation1function() = runBlocking {
    try {
        upstreamViolation1.collect { value ->
            Log.i(TAG,"Received $value")
            check(value <= 2) { "Collected $value while we expect values below 2" }
        }
    } catch (e: Throwable) {
        Log.e(TAG, "Caught $e")
    }
}