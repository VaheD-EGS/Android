package com.egs.ansroid.samples.lesson1

import android.util.Log
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class Lesson1 {

    // **************************** Sample 1 ******************************

    fun makeRequest1() = runBlocking {
        //Log.i(TAG,"${Thread.activeCount()} threads active at the start")

        val time = measureTimeMillis {
            createCoroutines(5)
        }

        //Log.i(TAG, "${Thread.activeCount()} threads active at the end")
        //Log.i(TAG, "Took $time ms")
    }

    suspend fun createCoroutines(amount: Int) {
        val jobs = ArrayList<Job>()
        for (i in 1..amount) {
            jobs += GlobalScope.launch {
                Log.i(TAG, "Started $i in ${Thread.currentThread().name}")
                delay(1000)
                Log.i(TAG, "Finished $i in ${Thread.currentThread().name}")
            }
        }
        jobs.forEach {
            it.join()
        }
    }

    // **************************** Sample 2 ******************************

    fun makeRequest2() {
        val words = listOf("level", "pope", "needle", "Anna", "Pete", "noon", "stats") // CPU Bound
        //val words = readWordsFromJson("resources/words.json") // I/O Bound
        filterPalindromes(words).forEach {
            Log.i(TAG, it)
        }
    }

    private fun isPalindrome(word: String) : Boolean {
        val lcWord = word.lowercase()
        return lcWord == lcWord.reversed()
    }

    private fun filterPalindromes(words: List<String>) : List<String> {
        return words.filter { isPalindrome(it) }
    }

    // **************************** Sample 3 ******************************
    data class UserInfo(val name: String, val lastName: String, val id: Int)
    lateinit var user: UserInfo

    fun makeRequest3() = runBlocking {
        asyncGetUserInfo(1)
        // Do some other operations
        delay(1000)
        Log.i(TAG,"User ${user.id} is ${user.name}")
    }

    fun asyncGetUserInfo(id: Int) = GlobalScope.async {
        delay(1100)
        user = UserInfo(id = id, name = "Susan", lastName = "Calvin")
    }

    // **************************** Sample 4 ******************************
    var counter = 0

    fun makeRequest4() = runBlocking {
        val workerA = asyncIncrement(2000)
        val workerB = asyncIncrement(100)
        workerA.await()
        workerB.await()
        Log.i(TAG,"counter [$counter]")
    }

    fun asyncIncrement(by: Int) = GlobalScope.async {
        for (i in 0 until by) {
            counter++
        }
    }

    // **************************** Sample 5 ******************************
    lateinit var jobA : Job
    lateinit var jobB : Job

    fun makeRequest5() = runBlocking {
        jobA = launch {
            delay(1000)
            // wait for JobB to finish
            jobB.join()
        }
        jobB = launch {
            // wait for JobA to finish
            jobA.join()
        }
        // wait for JobA to finish
        jobA.join()
        println("Finished")
    }

    companion object {
        const val TAG = "Lesson1"
    }
}