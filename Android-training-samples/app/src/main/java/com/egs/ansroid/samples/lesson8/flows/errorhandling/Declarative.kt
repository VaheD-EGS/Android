package com.egs.ansroid.samples.lesson8.flows.errorhandling

import android.util.Log
import com.egs.ansroid.samples.lesson8.Lesson8
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach

val upstream4 = flowOf(1, 2, 3)
val TAG= Lesson8::class.simpleName
val encapsulateError2 = upstream4.onEach {
        if (it > 2) throw RuntimeException()
    }.catch { e ->
        Log.i(TAG,"Caught $e")
    }
