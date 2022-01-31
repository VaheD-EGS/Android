package com.egs.ansroid.samples.lesson9

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.RuntimeException

class Lesson9 {
    val TAG = Lesson9::class.simpleName
    val scope = CoroutineScope(Job() + Dispatchers.IO)


    //=============================== Examples from previous lesson ===============================

    fun violatesTest() = runBlocking {
        val violatesExceptionTransparency: Flow<Int> = flow {

            for (i in 1..3) {
                try {
                    emit(i)
                } catch (e: Throwable) {
                    Log.e(TAG, "flow builder: ${e.message} ")
                    emit(-1)
                }
            }
        }

        /**
         * case 1. with out try catch
         * case 2. with try catch
         * case 3. throwing IllegalStateException without check :)
         * */
        violatesExceptionTransparency.collect { value ->
            try {
                //check(value <= 2) { "error because $value" }
                //Log.i(TAG, "received value is: $value")
                if (value <= 2) {
                    Log.i(TAG, "received value is: $value")
                } else {
                    throw IllegalStateException("exception message")
                }
            } catch (e: Throwable) {
                Log.e(TAG, "Cause: ${e.message}")
            }
        }
    }

    fun catchOperator() = runBlocking {
        val flow = flowOf(1, 2, 3)
        val encapsulateError = flow.onEach {
            if (it > 2) throw RuntimeException()
        }.catch {
            //e ->
//            Log.i(TAG, "${e.message}")
            this.emit(-1)
        }

    }

    fun fusibleFlowExample() = runBlocking {

    }
    //=============================================================================================


    /**
     * error during crating new scope inside flow builder
     */
    fun flowTest() = runBlocking {

        val flow = flow {
            val index = 0
            scope.launch {
                while (true) {
                    emit(index)
                }
            }
        }

        scope.launch {
            flow.collect {
                Log.i(TAG, "received index is: $it")
            }
        }
        delay(1000)
        scope.cancel()
    }

    /**
     * The launchIn operator returns a Job that can be used to cancel() the flow collection without canceling the whole coroutine scope.
     * If needed, you can use join() to wait for the job to complete.*/
    fun switchingScope() = runBlocking {
        val someCoroutineScope = CoroutineScope(Job())
        someCoroutineScope.launch {
            val job = (0..100).asFlow()
                .onEach { value ->
                    delay(100)
                    Log.i(TAG, "$value")
                }.launchIn(scope = scope)
            delay(1000)
            job.cancel()
            Log.i(TAG, "Operation is completed")
        }


        //======================= Zip ===============================
        fun zipTest() = runBlocking {
            val flowInt = flowOf(1, 2, 3, 5)
            val flowString = flowOf("A", "B", "C")
            flowInt.zip(flowString) { intValue, stringValue ->
                "$intValue$stringValue"
            }.collect {
                Log.d(TAG, it)
            }
        }
    }
}




