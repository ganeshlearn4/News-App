package com.newsapp.app.ui.fragments.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newsapp.data.Constants
import com.newsapp.data.model.Article
import com.newsapp.enums.RequestStatus
import com.newsapp.interfaces.DataHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiKey: String,
    private val repository: DataHelper
) : ViewModel() {
    private val pageSize = MutableLiveData(5)
    private val pageNumber = MutableLiveData(1)

    val totalArticlesLoaded = MutableLiveData<Int>(0)
    val totalArticles = MutableLiveData<Int>(0)

    val allArticles = MutableLiveData<ArrayList<Article>>()
    val newArticles = MutableLiveData<ArrayList<Article>>()

    val canLoadNewData = MutableLiveData<Boolean>(true)
    val isScrolling = MutableLiveData<Boolean>(false)

    var requestStatus = MutableLiveData<RequestStatus>(RequestStatus.NONE)

    init {
        getNews()
    }

    private fun getNews() {
        pageNumber.value = 1
        totalArticlesLoaded.value = 0
        newArticles.value?.clear()

        viewModelScope.launch {
            requestStatus.postValue(RequestStatus.LOADING)
            val response = repository.fetchNewsWithPagination(
                apiKey,
                pageSize.value!!,
                pageNumber.value!!,
                Constants.news_source
            )
            if (response.isSuccessful && response.body() != null && response.body()?.status ?: "" == "ok") {
                totalArticles.postValue(response.body()!!.totalResults)
                allArticles.postValue(ArrayList(response.body()!!.articles))
                newArticles.postValue(ArrayList(response.body()!!.articles))
                requestStatus.postValue(RequestStatus.SUCCESS)
            } else {
                requestStatus.postValue(RequestStatus.ERROR)
            }
        }
    }

    fun getMoreNews() {
        pageNumber.value = pageNumber.value?.plus(1)
        newArticles.value?.clear()

        viewModelScope.launch {
            requestStatus.postValue(RequestStatus.LOADING)
            val response = repository.fetchNewsWithPagination(
                apiKey,
                pageSize.value!!,
                pageNumber.value!!,
                Constants.news_source
            )
            if (response.isSuccessful && response.body() != null && response.body()?.status ?: "" == "ok") {
                newArticles.postValue(ArrayList(response.body()!!.articles))
                requestStatus.postValue(RequestStatus.SUCCESS)
            } else {
                requestStatus.postValue(RequestStatus.ERROR)
            }
        }
    }

    fun bookmarkArticle(article: Article) {
        Log.d("app", article.toString())
        viewModelScope.launch {
            repository.addArticle(article)
        }
    }
}