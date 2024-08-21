package com.rizqanmr.carousellnews.presentation.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rizqanmr.core.data.network.model.NewsNetwork
import com.rizqanmr.core.utils.NewsFilterType
import kotlinx.coroutines.Job

interface NewsViewModelContract {

    fun getIsLoading(): LiveData<Boolean>

    fun setIsLoading(isLoading: Boolean)

    fun getListNews(): Job

    fun listNewsLiveData(): MutableLiveData<List<NewsNetwork>>

    fun errorListNewsLiveData(): LiveData<String>

    fun setFilterType(filterType: NewsFilterType)
}