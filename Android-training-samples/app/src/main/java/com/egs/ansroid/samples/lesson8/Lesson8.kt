package com.egs.ansroid.samples.lesson8

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.egs.ansroid.samples.lesson8.flows.customOperators.bufferTimeout
import com.egs.ansroid.samples.lesson8.flows.sharedflow.eventbus.Downloader
import com.egs.ansroid.samples.lesson8.flows.sharedflow.eventbus.EventBus
import com.egs.ansroid.samples.lesson8.flows.errorhandling.encapsulateError
import com.egs.ansroid.samples.lesson8.flows.errorhandling.imperativeImpl1
import com.egs.ansroid.samples.lesson8.flows.errorhandling.innerExcViolation1function
import com.egs.ansroid.samples.lesson8.flows.messages.getMessagesFromUser
import com.egs.ansroid.samples.lesson8.flows.realisticexample.getDataFlow
import com.egs.ansroid.samples.lesson8.flows.usecase2.locationsFlow
import com.egs.ansroid.samples.lesson8.flows.usecase2.transform
import com.peterlaurence.book.pawk.flows.sharedflow.sharedreplay.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
class Lesson8 {
    val TAG = Lesson8::class.simpleName

    fun customOperator() = runBlocking {
        val flow = (1..100).asFlow().onEach { delay(10) }
        val startTime = System.currentTimeMillis()
        flow.bufferTimeout(10, 50).collect {
            val time = System.currentTimeMillis() - startTime
            Log.i(TAG, "$time ms: $it")
        }

    }

    fun encapsulatedError() = runBlocking {
        encapsulateError.collect {
            Log.i(TAG, "Received $it")
        }
    }

    fun encapsulatedError2() = runBlocking {
        try {
            encapsulateError.collect {
                if (it > 2) throw RuntimeException()
                Log.i(TAG, "Received $it")
            }
        } catch (e: RuntimeException) {
            Log.e(TAG, "Collector stopped collecting the flow")
        }
    }

    fun encapsulatedError3() = runBlocking {
        com.egs.ansroid.samples.lesson8.flows.errorhandling.declarative3.encapsulateError.collect {
            Log.i(TAG, "Received $it")
        }
    }

    fun exceptionViolation() = runBlocking {
        innerExcViolation1function()
    }

    fun exceptionViolation2() {
        exceptionViolation2()
    }

    fun imperative1() {
        imperativeImpl1()
    }

    fun getMessageFlow() = runBlocking {
        getMessagesFromUser("Amanda", "en-us").collect {
            Log.i(TAG, "Received message from ${it.user}: ${it.content}")
        }
    }

    fun getToken() = runBlocking<Unit> {
        val flow = getDataFlow(3)
        launch {
            flow.collect()
            flow.map { }
        }
    }

    fun downloader() = runBlocking {
        val eventBus = EventBus()

        delay(100)
        Log.i(com.egs.ansroid.samples.lesson8.flows.errorhandling.TAG, "start download")
        Downloader(eventBus, this)
        eventBus.startDownload("http://somewebsite_link")
        Unit
    }

    fun sharedReplay() = runBlocking {
        val dao = object : NewsDao {
            private var index = 0

            override suspend fun fetchNewsFromApi(): List<News> {
                delay(100)
                return listOf(News("news content ${++index}"), News("news content ${++index}"))
            }
        }
        val repo = NewsRepository(dao)
        NewsViewsModel(repo)
        delay(150)
        AnotherViewModel(repo)

        delay(30_000)
        repo.stop()
    }


    fun eventBus() = runBlocking {
        val eventBus = EventBus()

        delay(100)
        Log.i(TAG, "start download")
        Downloader(eventBus, this)
        eventBus.startDownload("http://somewebsite_link")
        Unit
    }

    fun unbufferedSharedFlow()  = runBlocking {
        val sharedFlow = MutableSharedFlow<String>()

        launch { // First subscriber
            sharedFlow.collect {
                Log.i(TAG, "Subscriber 1 receives $it")
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

    fun usecase2() = runBlocking {
        // Defining the Flow of Content - nothing is executing yet
        val contentFlow = locationsFlow.map { loc ->
            flow {
                emit(transform(loc))
            }
        }.flattenMerge(4)

        // We now collect the entire flow using the toList terminal operator
        val contents = contentFlow.toList()
    }
    //===================================================================


}