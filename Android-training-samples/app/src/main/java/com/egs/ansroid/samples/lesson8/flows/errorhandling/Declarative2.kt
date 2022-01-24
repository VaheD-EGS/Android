package com.egs.ansroid.samples.lesson8.flows.errorhandling

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking

val upstream = flowOf(1, 3, -1)

val encapsulateError = upstream.onEach {
        if (it < 0) throw NumberFormatException("Values should be greater than 0")
    }.catch { e ->
        println("Caught $e")
    }