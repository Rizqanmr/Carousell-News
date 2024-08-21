package com.rizqanmr.core.data.repository

import com.rizqanmr.core.data.network.Result
import com.rizqanmr.core.data.network.model.NewsNetwork

interface NewsRepository {

    suspend fun getListNews(): Result<List<NewsNetwork>>
}