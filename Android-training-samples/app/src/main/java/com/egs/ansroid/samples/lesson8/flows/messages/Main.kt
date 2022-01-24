package com.egs.ansroid.samples.lesson8.flows.messages

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime


data class Message(
    val user: String,
    val date: LocalDateTime,
    val content: String
)

@RequiresApi(Build.VERSION_CODES.O)
fun getMessageFlow(): Flow<Message> = flow {
    emit(Message("Amanda", LocalDateTime.now(), "First msg"))
    emit(Message("Amanda", LocalDateTime.now(), "Second msg"))
    emit(Message("Pierre", LocalDateTime.now(), "First msg"))
    emit(Message("Amanda", LocalDateTime.now(), "Third msg"))
}

fun getMessageFlow(factory: MessageFactory) = callbackFlow<Message> {
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

@RequiresApi(Build.VERSION_CODES.O)
fun getMessagesFromUser(user: String, language: String): Flow<Message> {
    return getMessageFlow()
        .filter { it.user == user }
        .map { it.translate(language) }
        .flowOn(Dispatchers.Default)
}

private suspend fun Message.translate(language: String): Message =
    withContext(Dispatchers.Default) {
        copy(content = "translated content")
    }