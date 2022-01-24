package com.egs.ansroid.samples.lesson8.flows.errorhandling.declarative3

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach

val upstream = flowOf(1, 3, -1)

val encapsulateError = upstream
    .onEach {
        if (it < 0) throw NumberFormatException("Values should be greater than 0")
    }
    .catch { e ->
        emit(0)
    }
