package com.rizqanmr.carousellnews.presentation.main.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rizqanmr.core.data.network.Result
import com.rizqanmr.core.data.network.model.NewsNetwork
import com.rizqanmr.core.data.repository.NewsRepository
import com.rizqanmr.core.data.repository.NewsRepositoryImpl
import com.rizqanmr.core.utils.NewsFilterType
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class NewsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var testDispatcher: TestDispatcher
    private lateinit var testScope: TestScope
    private lateinit var repository: NewsRepository
    private lateinit var viewmodel: NewsViewModel

    @Before
    fun setup() {
        testDispatcher = StandardTestDispatcher()
        testScope = TestScope(testDispatcher)
        Dispatchers.setMain(testDispatcher)
        repository = mockk<NewsRepositoryImpl>()
        viewmodel = NewsViewModel(repository)
        viewmodel.listNewsLiveData().observeForever {  }
        viewmodel.errorListNewsLiveData().observeForever {  }
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `getListNews should post value on success`() = testScope.runTest {
        // Given
        val fakeNewsNetwork = fakeNewsNetwork()
        coEvery { repository.getListNews() } returns Result.Success(fakeNewsNetwork)

        // When
        viewmodel.getListNews()
        advanceUntilIdle()

        // Then
        val expected = fakeNewsNetwork.sortedByDescending { it.timeCreated }
        assertEquals(expected, viewmodel.listNewsLiveData().value)
    }

    @Test
    fun `getListNews should post error on failure`() = testScope.runTest {
        // Given
        val errorMessage = "Error message"
        coEvery { repository.getListNews() } returns Result.Error(Exception(errorMessage))

        // When
        viewmodel.getListNews()
        advanceUntilIdle()

        // Then
        assertEquals(errorMessage, viewmodel.errorListNewsLiveData().value)
    }

    @Test
    fun `applyFilter should update listNewsLiveData with correct filter`() = testScope.runTest {
        // Given
        val fakeNewsNetwork = fakeNewsNetwork()
        coEvery { repository.getListNews() } returns Result.Success(fakeNewsNetwork)

        viewmodel.setFilterType(NewsFilterType.POPULAR)

        // When
        viewmodel.getListNews()
        advanceUntilIdle()

        // Then
        val expected = fakeNewsNetwork.sortedWith(compareBy<NewsNetwork> { it.rank }
            .thenByDescending { it.timeCreated })
        assertEquals(expected, viewmodel.listNewsLiveData().value)
    }

    companion object {
        fun fakeNewsNetwork() = listOf(
            NewsNetwork(id = "1", title = "News 1", description = "desc 1", timeCreated = 1234567890, rank = 1, bannerUrl = "image 1"),
            NewsNetwork(id = "2", title = "News 2", description = "desc 2", timeCreated = 9876543210, rank = 2, bannerUrl = "image 2")
        )
    }
}