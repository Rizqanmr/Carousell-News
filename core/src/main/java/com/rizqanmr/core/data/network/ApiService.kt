package com.rizqanmr.core.data.network

import com.rizqanmr.core.data.network.model.NewsNetwork
import retrofit2.http.GET

interface ApiService {

    @GET("carousell_news.json")
    suspend fun getListNews(): List<NewsNetwork>
}