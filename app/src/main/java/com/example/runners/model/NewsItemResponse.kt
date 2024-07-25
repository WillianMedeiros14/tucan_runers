package com.example.runners.model

data class NewsItemResponse(
    val status: String,
    val totalResults: Number,
    val articles: List<NewsResponseData>
)

data class Source(
    val id: String?,
    val name: String
)

data class NewsResponseData(
    val source: Source,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
)


