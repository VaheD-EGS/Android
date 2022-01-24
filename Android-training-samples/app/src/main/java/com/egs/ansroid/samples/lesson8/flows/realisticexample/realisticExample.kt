package com.egs.ansroid.samples.lesson8.flows.realisticexample

import android.util.Log
import com.egs.ansroid.samples.lesson8.flows.errorhandling.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun getDataFlow(n: Int): Flow<TokenData>{
    return flow {
        connect()
        repeat(n) {
            val token = getToken()
            val data = getData(token)
            emit(TokenData(token, data))
        }
    }.onCompletion {
        disconnect()
    }
}


private suspend fun connect() {
    Log.i(TAG,"Connecting..")
    delay(10)
}

private suspend fun getToken(): String {
   Log.i(TAG,"Getting token..")
    delay(15)
    return "token"
}

private suspend fun getData(token: String): String? {
   Log.i(TAG,"Getting data for $token")
    delay(5)
    return "data"
}

private fun disconnect() {
   Log.i(TAG,"Disconnect")
}

data class TokenData(val token: String, val data: String? = null)