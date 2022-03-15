package com.newsapp.data.repository

import androidx.lifecycle.LiveData
import com.newsapp.data.model.Article
import com.newsapp.data.model.NewsApiResponse
import com.newsapp.data.network.NewsApiHelper
import com.newsapp.data.room.Database
import com.newsapp.interfaces.DataHelper
import retrofit2.Response

class DataRepository(private val apiHelper: NewsApiHelper, private val database: Database): DataHelper {
    fun returnDataHelper(): DataHelper {
        return this
    }

    override suspend fun fetchNewsWithPagination(
        apiKey: String,
        pageSize: Int,
        pageNumber: Int,
        sources: String
    ): Response<NewsApiResponse> {
        return apiHelper.fetchNewsWithPagination(apiKey, pageSize, pageNumber, sources)
    }

    override suspend fun searchNews(apiKey: String, query: String): Response<NewsApiResponse> {
        return apiHelper.searchNews(apiKey, query)
    }

    override suspend fun addArticle(article: Article) {
        database.articleDao.addArticle(article)
    }

    override suspend fun containsArticleTitle(articleTitle: String): Boolean {
        return database.articleDao.containsArticleTitle(articleTitle)
    }

    override suspend fun deleteArticle(article: Article) {
        database.articleDao.deleteArticle(article)
    }

    override suspend fun getAll(): LiveData<List<Article>> {
        return database.articleDao.getAll()
    }
}