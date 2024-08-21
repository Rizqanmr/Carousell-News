package com.rizqanmr.carousellnews.presentation.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizqanmr.core.data.network.Result
import com.rizqanmr.core.data.network.model.NewsNetwork
import com.rizqanmr.core.data.repository.NewsRepository
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

    private val listNewsLiveData: MutableLiveData<List<NewsNetwork>> = MutableLiveData()
    private val errorListLiveData: MutableLiveData<String> = MutableLiveData()

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext

    override fun getListNews(): Job = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            when (val result = repository.getListNews()) {
                is Result.Success -> listNewsLiveData.postValue(result.data)
                is Result.Error -> errorListLiveData.postValue(result.exception.localizedMessage)
            }
        }
    }

    override fun listNewsLiveData(): MutableLiveData<List<NewsNetwork>> = listNewsLiveData

    override fun errorListNewsLiveData(): LiveData<String> = errorListLiveData
}