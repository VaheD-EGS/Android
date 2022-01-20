package com.egs.ansroid.samples.lesson7

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.io.IOException
import java.lang.RuntimeException
import java.time.LocalDateTime
import java.util.*
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
class Lesson7 {

    data class Message(
        val user: String,
        val date: LocalDateTime,
        val content: String
    )

    private val TAG = Lesson7::class.simpleName
    private fun getMessageFlow(): Flow<Message> = flow {
        emit(Message("Amanda", LocalDateTime.now(), "First msg"))
        emit(Message("Amanda", LocalDateTime.now(), "Second msg"))
        emit(Message("Pierre", LocalDateTime.now(), "First msg"))
        emit(Message("Amanda", LocalDateTime.now(), "Third msg"))
    }

    fun getMessagesFromUser(user: String, language: String): Flow<Message> {
        return getMessageFlow()
//            .filter {
//                Log.i(TAG, "Filter operator is running in ${Thread.currentThread().name}")
//                it.user == user
//            }
            .map {
                Log.i(TAG, "Map operator is running in ${Thread.currentThread().name}")
                it.translate(language)
            }
            .flowOn(Dispatchers.Default)

    }

    private suspend fun Message.translate(language: String): Message =
        withContext(Dispatchers.Main) {
//            copy(content = "translated content")
            Log.i(TAG, "translate function ${Thread.currentThread().name}")
            Message("",LocalDateTime.now(),"")

        }

    fun getMessage() {
        runBlocking {
            getMessagesFromUser("Amanda", "en-us").collect {
                Log.i(TAG, "Received message from ${it.user}: ${it.content}")
            }
        }
    }

    fun getMessageFlow(factory: MessageFactory) = callbackFlow {
        val observer = object : MessageFactory.MessageObserver {
            override fun onMessage(msg: Message) {
                trySend(msg)
            }

            override fun onCancelled() {
                channel.close()
            }

            override fun onError(cause: Throwable) {
                cancel(CancellationException("Message factory error", cause))
            }
        }

        factory.registerObserver(observer)
        awaitClose {
            factory.unregisterObserver(observer)
        }
    }

    val testFlow = flow<Int> {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            emit(100)
            Log.i("Lesson7", ">>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<")
        }
    }


    //========================_operator_==========================
    fun operatorsUsage() {
        var flow = flow {
            for (i in 0..10) {
                emit(i)
                delay(1000L)
            }
        }

        GlobalScope.launch {
            flow.filter {
                it % 2 == 0
            }.collect {
                Log.i(TAG, "Received value is $it")
                delay(2000L)
            }
        }
    }
    //============================================================

    //============================_Custom Operator_================================
    fun customOperator() {
        GlobalScope.launch {
            val flow = (1..100).asFlow().onEach {
                delay(10)
            }
            val startTime = System.currentTimeMillis()
            flow.bufferTimeout(10, 50).collect {
                val time = System.currentTimeMillis() - startTime
                Log.i(TAG, "$time ms: $it")
            }
        }

    }
    //============================================================

    //===========================_flattenMerge_==???????=================
    class Location
    class Content(var name: String)

    suspend fun transform(loc: Location): Content = withContext(Dispatchers.IO) {
        println("transform")
        Content("${Random.nextInt()}")
    }

    private val locationsFlow = flowOf(Location(), Location())

    fun margeflatte() = runBlocking {
        // Defining the Flow of Content - nothing is executing yet
        val contentFlow = locationsFlow.map { loc ->
            flow {
                emit(transform(loc))
            }
        }.flattenMerge(4)

        // We now collect the entire flow using the toList terminal operator
        val contents = contentFlow.toList()
        contents.forEach {
            Log.i(TAG, it.name)
        }
    }

    //============================================================


    //===========================_Try Catch_=======================
    fun exceptionExperiment1() {
        val upstream = flowOf(1, 2, 3, 4, 5, 6).onEach {
            if (it > 3) throw RuntimeException()
        }
        GlobalScope.launch {
            try {
                upstream.collect { value ->
                    Log.i(TAG, "Received $value")
                }
            } catch (e: Throwable) {
                Log.i(TAG, "Caught $e")
            }
        }
    }

    /**
     * Warning: DON'T DO THIS, this flow swallows downstream exceptions
     * */


    fun exceptionExperiment2() {
        val upstream: Flow<Int> = flow {
            for (i in 1..3) {
                try {
                    //same logic
                    //.....
                    emit(i)
                } catch (e: Throwable) {
                    Log.e(TAG, "Caught in flow builder: $e")
                }
            }
        }
        GlobalScope.launch {
            try {
                upstream.collect { value ->
                    Log.i(TAG, "Received $value")
                    check(value <= 2) {
                        "Collected $value while we expect values below 2"
                    }
                }
            } catch (e: Throwable) {
                Log.e(TAG, "Caught: $e")
            }
        }
    }

    fun exceptionExperiment3(){
        val violatesExceptionTransparency: Flow<Int> = flow {
            for (i in 1..3) {
                try {
                    emit(i)
                } catch (e: Throwable) {
                    emit(-1)
                }
            }
        }

        GlobalScope.launch {
            try {
                violatesExceptionTransparency.collect { value ->
                    check(value <= 2) { "Collected $value" }
                }
            } catch (e: Throwable) {
                Log.i(TAG, "Caught $e")
            }
        }
    }
    //=============================================================


    //===========================_catch operator _==================================

    fun catchOperator(){
        val upstream = flowOf(1, 3, -1)
        val encapsulateError = upstream
            .onEach {
                if (it < 0) throw NumberFormatException("Values should be greater than 0")
            }
            .catch { e ->
                Log.e(TAG, "Caught $e")

            }
        GlobalScope.launch {
            try {
                encapsulateError.collect{
                    if(it > 2 ){
                        throw RuntimeException()
                    }
                    Log.i(TAG, "$it")
                }
            }catch(e: RuntimeException){
                Log.e(TAG, "${e.message}")
            }


        }
    }
    //=============================================================


}