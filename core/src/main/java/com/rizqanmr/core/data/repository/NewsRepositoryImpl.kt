package com.rizqanmr.core.data.repository

import com.rizqanmr.core.data.network.ApiService
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : NewsRepository {
}