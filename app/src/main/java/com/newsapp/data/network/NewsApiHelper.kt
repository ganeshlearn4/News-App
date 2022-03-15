package com.newsapp.data.network

import com.newsapp.data.model.NewsApiResponse
import retrofit2.Response

class NewsApiHelper(private val newsApi: NewsApi) {
    suspend fun fetchNewsWithPagination(
        apiKey: String,
        pageSize: Int,
        pageNumber: Int,
        sources: String
    ): Response<NewsApiResponse> {
        return newsApi.fetchNewsWithPagination(apiKey, pageSize, pageNumber, sources)
    }

    suspend fun searchNews(apiKey: String, query: String): Response<NewsApiResponse> {
        return newsApi.searchNews(apiKey, query)
    }
}