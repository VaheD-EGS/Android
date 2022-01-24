package com.egs.ansroid.samples.lesson8.flows.errorhandling

import android.util.Log
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking

val upstream2 = flowOf(1, 2, 3)

fun imperativeImpl1() = runBlocking {
    try {
        upstream2.collect { value ->
            if (value > 2) {
                throw RuntimeException()
            }
            Log.i(TAG,"Received $value")
        }
    } catch (e: Throwable) {
        Log.i(TAG,"Caught $e")
    }
}

