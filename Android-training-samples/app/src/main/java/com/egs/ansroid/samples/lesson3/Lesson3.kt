package com.egs.ansroid.samples.lesson3

import android.util.Log
import kotlinx.coroutines.*

class Lesson3 {
    // *********************** Sample 1 ***********************

    fun makeRequest1() = runBlocking {
        val scope = CoroutineScope(Job())
        val job = scope.launch {
            coroutineScope {
                val task1 = launch {
                    delay(1000)
                    Log.i(TAG,"Done background task")
                }
                val task2 = async {
                    throw Exception()
                    1
                }
                try {
                    task2.await()
                } catch (e: Exception) {
                    Log.i(TAG,"Caught exception $e")
                }
                task1.join()
            }
        }
        job.join()
        Log.i(TAG, "Program ends")
    }

    companion object {
        const val TAG = "Lesson3"
    }
}