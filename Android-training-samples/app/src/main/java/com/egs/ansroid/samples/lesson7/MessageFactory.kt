package com.egs.ansroid.samples.lesson7

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.*

abstract class MessageFactory : Thread() {
    private val observers = Collections.synchronizedList(mutableListOf<MessageObserver>())
    private var isActive = true

    /**
     * Periodically fetches the message and sends to the observers via onMessage
     * */
    override fun run() = runBlocking {
        while(isActive) {
            val message = fetchMessage()
            for (observer in observers) {
                observer.onMessage(message)
            }
            delay(1000)
        }
    }

    /**
     * Intended for fetching the message
     * */
    abstract fun fetchMessage(): Lesson7.Message

    /**
     * adds the selected observer to the observers list
     * */
    fun registerObserver(observer: MessageObserver) {
        observers.add(observer)
    }

    /**
     * Removes the selected observers if there are any
     * */
    fun unregisterObserver(observer: MessageObserver) {
        observers.removeAll { it == observer }
    }

    /**
     * Makes isActive inactive cancels all observers and clears the observers list
     * */
    fun cancel() {
        isActive = false
        observers.forEach {
            it.onCancelled()
        }
        observers.clear()
    }

    /**
     * Intended for handling message receiving, cancellation and error cases
     * */
    interface MessageObserver {
        fun onMessage(msg: Lesson7.Message)
        fun onCancelled()
        fun onError(cause: Throwable)
    }
}