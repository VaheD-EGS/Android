package com.peterlaurence.book.pawk.flows.sharedflow.unbuffered

import android.util.Log
import com.egs.ansroid.samples.lesson8.flows.errorhandling.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val sharedFlow = MutableSharedFlow<String>()

    launch { // First subscriber
        sharedFlow.collect {
           Log.i(TAG,"Subscriber 1 receives $it")
        }
    }

    launch { // Second subscriber - slow
        sharedFlow.collect {
            Log.i(TAG,"Subscriber 2 receives $it")
            delay(3000)
        }
    }

    launch { // Start emitting values
        sharedFlow.emit("one")
        sharedFlow.emit("two")
        sharedFlow.emit("three")
    }

    Unit
}
