package com.peterlaurence.book.pawk.flows.sharedflow.sharedreplay

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect

class NewsRepository(private val dao: NewsDao) {
    private val _newsFeed = MutableSharedFlow<News>()
    val newsFeed = _newsFeed.asSharedFlow()

    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    init {
        scope.launch {
            while (true) {
                val news = dao.fetchNewsFromApi()
                scope.launch {
                    news.forEach { _newsFeed.emit(it) }
                }

                delay(3000) // wait 30s
            }
        }
    }

    fun stop() = scope.cancel()
}

interface NewsDao {
    suspend fun fetchNewsFromApi(): List<News>
}

data class News(val content: String)

class NewsViewsModel(private val repository: NewsRepository) {
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    init {
        viewModelScope.launch {
            repository.newsFeed.collect {
                println("NewsViewsModel receives $it")
            }
        }
    }
}

/**
 * This is the version you would use in a real Android application
 */
//class NewsViewsModel @ViewModelInject constructor(
//    private val repository: NewsRepository
//) : ViewModel() {
//    private val newsList = mutableListOf<News>()
//
//    private val _newsLiveData = MutableLiveData<List<News>>(newsList)
//    val newsLiveData: LiveData<List<News>> = _newsLiveData
//
//    init {
//        viewModelScope.launch {
//            repository.newsFeed.collect {
//                println("NewsViewsModel receives $it")
//                newsList.add(it)
//                _newsLiveData.value = newsList
//            }
//        }
//    }
//}

class AnotherViewModel(private val repository: NewsRepository) {
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    init {
        viewModelScope.launch {
            repository.newsFeed.collect {
                println("AnotherViewModel receives $it")
            }
        }
    }
}