package com.egs.ansroid.samples.lesson8.flows.usecase2

import android.util.Log
import com.egs.ansroid.samples.lesson8.flows.errorhandling.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class Location
class Content

suspend fun transform(loc: Location): Content = withContext(Dispatchers.IO) {
    Log.i(TAG,"transform")
    Content()
}

val locationsFlow = flowOf(Location(), Location())
