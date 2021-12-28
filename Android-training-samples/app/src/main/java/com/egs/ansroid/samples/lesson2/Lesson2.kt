package com.egs.ansroid.samples.lesson2

import android.util.Log
import com.egs.ansroid.samples.lesson1.Lesson1
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

class Lesson2 {
    // *********************** Sample 1 ***********************
    fun makeRequest1() = runBlocking {
        val time = measureTimeMillis {
            val name = getName()
            val lastName = getLastName()
            Log.i(TAG,"Hello, $name $lastName")
        }
        Log.i(TAG,"Execution took $time ms")
    }

    suspend fun getName(): String {
        delay(1000)
        return "Susan"
    }

    suspend fun getLastName(): String {
        delay(1000)
        return "Calvin"
    }

    fun makeRequest1_1() = runBlocking {
        val time = measureTimeMillis {
            val name = async { getName() }
            val lastName = async { getLastName() }
            Log.i(TAG,"Hello, ${name.await()} ${lastName.await()}")
        }

        Log.i(TAG,"Execution took $time ms")
    }

    //****************** Sample 2 **********************
    data class User(val userId: String, val name: String)

    fun makeRequest2(userId: String): User {
        Thread.sleep(1000)
        return User(userId, "Filip")
    }

    // **************** Sample 3 ***********************
    fun makeRequest3() {
        getUserFromNetworkCallback("101") { user ->
            Log.i(TAG, user.toString())
        }
        Log.i(TAG,"main end")
    }

    private fun getUserFromNetworkCallback(
        userId: String,
        onUserReady: (User) -> Unit) {
        thread {
            Thread.sleep(1000)
            val user = User(userId, "Filip")
            onUserReady(user)
        }
        Log.i(TAG,"end")
    }

    // *************** Sample 4 ***********************
    fun makeRequest4() {
        getUserFromNetworkCallback4("101") { user, error ->
            user?.run(::println)
            error?.printStackTrace()
        }
    }

    fun getUserFromNetworkCallback4(
        userId: String,
        onUserResponse: (User?, Throwable?) -> Unit) {
        thread {
            try {
                Thread.sleep(1000)
                val user = User(userId, "Filip")
                onUserResponse(user, null)
            } catch (error: Throwable) {
                onUserResponse(null, error)
            }
        }
    }

    // **************** Sample 5 *************************
    suspend fun getUserSuspend(userId: String): User {
        delay(1000)
        return User(userId, "Filip")
    }

    companion object {
        const val TAG = "Lesson2"
    }
}