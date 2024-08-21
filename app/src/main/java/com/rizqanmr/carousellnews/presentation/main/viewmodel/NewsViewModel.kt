package com.rizqanmr.carousellnews.presentation.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizqanmr.core.data.network.Result
import com.rizqanmr.core.data.network.model.NewsNetwork
import com.rizqanmr.core.data.repository.NewsRepository
import com.rizqanmr.core.utils.NewsFilterType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : NewsViewModelContract, CoroutineScope, ViewModel() {

    private val isLoading = MutableLiveData<Boolean>()
    private val listNewsLiveData: MutableLiveData<List<NewsNetwork>> = MutableLiveData()
    private val errorListLiveData: MutableLiveData<String> = MutableLiveData()
    private var listNews: List<NewsNetwork> = emptyList()
    private val _filterType = MutableLiveData(NewsFilterType.RECENT)

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext

    init {
        getListNews()
    }

    override fun getIsLoading(): LiveData<Boolean> = isLoading

    override fun setIsLoading(isLoading: Boolean) {
        this.isLoading.value = isLoading
    }

    override fun getListNews(): Job = viewModelScope.launch {
        setIsLoading(true)

        withContext(Dispatchers.IO) {
            when (val result = repository.getListNews()) {
                is Result.Success -> {
                    listNews = result.data
                    applyFilter()
                }
                is Result.Error -> errorListLiveData.postValue(result.exception.localizedMessage)
            }
        }

        setIsLoading(false)
    }

    override fun listNewsLiveData(): MutableLiveData<List<NewsNetwork>> = listNewsLiveData

    override fun errorListNewsLiveData(): LiveData<String> = errorListLiveData

    override fun setFilterType(filterType: NewsFilterType) {
        _filterType.value = filterType
        applyFilter()
    }

    private fun applyFilter() {
        val filteredNews = when (_filterType.value) {
            NewsFilterType.RECENT -> listNews.sortedByDescending { it.timeCreated }
            NewsFilterType.POPULAR -> listNews.sortedWith(compareBy<NewsNetwork> { it.rank }
                .thenByDescending { it.timeCreated })
            else -> listNews
        }

        listNewsLiveData.postValue(filteredNews)
    }
}