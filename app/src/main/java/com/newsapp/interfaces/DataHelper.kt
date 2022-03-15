package com.newsapp.interfaces

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.newsapp.data.model.Article
import com.newsapp.data.model.NewsApiResponse
import retrofit2.Response

interface DataHelper {
    suspend fun fetchNewsWithPagination(
        apiKey: String,
        pageSize: Int,
        pageNumber: Int,
        sources: String
    ): Response<NewsApiResponse>

    suspend fun searchNews(apiKey: String, query: String): Response<NewsApiResponse>


    suspend fun addArticle(article: Article)

    suspend fun containsArticleTitle(articleTitle: String): Boolean

    suspend fun deleteArticle(article: Article)

    suspend fun getAll(): LiveData<List<Article>>
}