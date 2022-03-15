package com.newsapp.data.network

import com.newsapp.data.model.NewsApiResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("everything")
    suspend fun fetchNewsWithPagination(@Query("apiKey") apiKey: String, @Query("pageSize") pageSize: Int, @Query("page") pageNumber: Int, @Query("sources") sources: String): Response<NewsApiResponse>

    @GET("everything")
    suspend fun searchNews(@Query("apiKey") apiKey: String, @Query("q") query: String): Response<NewsApiResponse>
}