package com.egs.ansroid.samples.lesson8.flows.errorhandling

import android.util.Log
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking

val upstream3 = flowOf(1, 2, 3)
    .onEach { value ->
        if (value > 2) throw RuntimeException()
    }

fun imperativeImpl2() = runBlocking {
    try {
        upstream3.collect { value ->
            Log.i(TAG,"Received $value")
        }
    } catch (e: Throwable) {
        Log.e(TAG, "Caught $e")
    }
}