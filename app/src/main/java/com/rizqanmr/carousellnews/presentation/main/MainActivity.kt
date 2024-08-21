package com.rizqanmr.carousellnews.presentation.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rizqanmr.carousellnews.databinding.ActivityMainBinding
import com.rizqanmr.carousellnews.databinding.ItemNewsBinding
import com.rizqanmr.carousellnews.presentation.adapter.NewsAdapter
import com.rizqanmr.carousellnews.presentation.main.viewmodel.NewsViewModel
import com.rizqanmr.core.data.network.model.NewsNetwork
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var newsAdapter: NewsAdapter
    private val viewmodel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        newsAdapter = NewsAdapter()
        setupRecyclerView()
        setupObservers()
        selectedNews()
        viewmodel.getListNews()
    }

    private fun setupRecyclerView() {
        binding.rvNews.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = newsAdapter
        }
    }

    private fun selectedNews() {
        newsAdapter.setUserListener(object : NewsAdapter.NewsListener {
            override fun onItemClick(itemNewsBinding: ItemNewsBinding, news: NewsNetwork?) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setupObservers() {
        viewmodel.listNewsLiveData().observe(this) {
            newsAdapter.saveData(it)
        }
        viewmodel.errorListNewsLiveData().observe(this) {
            Snackbar.make(binding.root, it.toString(), Snackbar.LENGTH_SHORT).show()
        }
    }
}