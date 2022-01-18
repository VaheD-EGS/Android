package com.egs.ansroid.samples.lesson4

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Lesson4 {
    // *********************** Sample 1 ***********************
    fun makeRequest1() = runBlocking {
        val channel = Channel<Item>()
        launch {
            channel.send(Item(1))
            channel.send(Item(2))
            Log.i(TAG,"Done sending")
        }
        Log.i(TAG, channel.receive().toString())
        Log.i(TAG, channel.receive().toString())
        Log.i(TAG,"Done!")
    }

    data class Item(val number: Int)

    // *********************** Sample 2 ***********************
    fun makeRequest2() = runBlocking {
        val channel = Channel<Item>()
        launch {
            channel.send(Item(1))
            channel.send(Item(2))
            Log.i(TAG,"Done sending")
            channel.close()
        }

        for (x in channel) {
            Log.i(TAG, x.toString())
        }
        Log.i(TAG,"Done!")
    }

    // *********************** Sample 3 ***********************
    fun makeRequest3() = runBlocking {
        val channel = Channel<Int>(UNLIMITED)
        val childJob = launch(Dispatchers.Default) {
            Log.i(TAG,"Child executing from ${Thread.currentThread().name}")
            var i = 0
            while (isActive) {
                channel.send(i++)
            }
            Log.i(TAG,"Child is done sending")
        }

        Log.i(TAG,"Parent executing from ${Thread.currentThread().name}")

        for (x in channel) {
            Log.i(TAG, x.toString())
            if (x == 1_000_000) {
                childJob.cancel()
                break
            }
        }
        Log.i(TAG,"Done!")
    }

    // *********************** Sample 4 ***********************
    fun makeRequest4() = runBlocking {
        val channel = Channel<String>(Channel.CONFLATED)
        val job = launch {
            channel.send("one")
            channel.send("two")
        }

        job.join()
        val elem = channel.receive()
        Log.i(TAG,"Last value was: $elem")
    }

    // *********************** Sample 5 ***********************
    fun makeRequest5() = runBlocking<Unit> {
        val channel = Channel<Int>(2)

        launch {
            for (i in 0..4) {
                Log.i(TAG,"Send $i")
                channel.send(i)
            }
        }

        launch {
            for (i in channel) {
                Log.i(TAG,"Received $i")
            }
        }
    }

    companion object {
        const val TAG = "Lesson4"
    }
}