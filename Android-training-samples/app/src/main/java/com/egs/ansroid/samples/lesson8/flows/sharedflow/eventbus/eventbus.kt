package com.egs.ansroid.samples.lesson8.flows.sharedflow.eventbus

import android.util.Log
import com.egs.ansroid.samples.lesson8.flows.errorhandling.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EventBus {
    private val _startDownloadEvent = MutableSharedFlow<DownloadEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val startDownloadEvent = _startDownloadEvent.asSharedFlow()

    fun startDownload(url: String) = _startDownloadEvent.tryEmit(DownloadEvent(url))
}

data class DownloadEvent(val url: String)

class Downloader(private val eventBus: EventBus, val scope: CoroutineScope) {
    init {
        scope.launch {
            Log.i(TAG,"susbscibe")
            eventBus.startDownloadEvent.collect {
                download(it.url)
            }
        }
    }

    private fun download(url: String) {
        Log.i(TAG,"Downloading $url..")
    }
}
