package com.newsapp.app.ui.fragments.bookmarks

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newsapp.data.model.Article
import com.newsapp.interfaces.DataHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(private val repository: DataHelper) : ViewModel() {
    lateinit var bookMarkedArticles: LiveData<List<Article>>

    init {
        viewModelScope.launch {
            bookMarkedArticles = repository.getAll()
        }
    }

    fun deleteBookMarkedArticle(article: Article) {
        viewModelScope.launch {
            repository.deleteArticle(article)
        }
    }
}