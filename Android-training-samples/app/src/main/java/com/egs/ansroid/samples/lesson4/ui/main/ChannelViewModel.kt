package com.egs.ansroid.samples.lesson4.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egs.ansroid.samples.lesson4.data.Location
import com.egs.ansroid.samples.lesson4.data.Shape
import com.egs.ansroid.samples.lesson4.data.ShapeCollector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.random.Random

class ChannelViewModel : ViewModel() {
    val TAG = "Lesson4"

    fun getShapesByLocations() = viewModelScope.launch {
        val shapes = Channel<Shape>()
        val locations = Channel<Location>()

        with(ShapeCollector(4)) {
            start(locations, shapes)
            consumeShapes(shapes)
        }
        sendLocations(locations)
    }

    var count = 0
    fun CoroutineScope.consumeShapes( shapesInput: ReceiveChannel<Shape>) =
        launch {
            for (shape in shapesInput) {
                // increment a counter of shapes
                count++
            }
        }

    fun CoroutineScope.sendLocations( locationsOutput: SendChannel<Location>) =
        launch {
            withTimeoutOrNull(3000) {
                while (true) {
                    /* Simulate fetching some shape location */
                    val location = Location(Random.nextInt(), Random.nextInt())
                    locationsOutput.send(location)
                }
            }

            Log.i(TAG, "Received $count shapes")
        }
}
