package com.example.runners.helper

import com.example.runners.model.NewsItemResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("everything")
    fun getNews(
        @Query("q") query: String = "Atletismo",
        @Query("searchIn") searchIn: String = "title,description,content",
        @Query("language") language: String = "pt",
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("apiKey") apiKey: String
    ): Call<NewsItemResponse>
}