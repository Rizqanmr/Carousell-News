package com.rizqanmr.carousellnews.presentation.main

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rizqanmr.carousellnews.R
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
        setSupportActionBar(binding.toolbar)
        setupRecyclerView()
        setupObservers()
        selectedNews()
        viewmodel.getListNews()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_menu -> {
                showSortMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
                Toast.makeText(
                    itemNewsBinding.cvNews.context,
                    "News rank ${news?.rank} clicked",
                    Toast.LENGTH_SHORT
                ).show()
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
        viewmodel.getIsLoading().observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.layoutLoading.progressLoading.isVisible = isLoading
    }

    private fun showSortMenu() {
        val view = findViewById<View>(R.id.action_menu) ?: return
        PopupMenu(this, view).apply {
            menuInflater.inflate(R.menu.menu_sort, menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_popular -> {
                        // Handle sort popular
                        true
                    }
                    R.id.action_recent -> {
                        // Handle sort recent
                        true
                    }
                    else -> false
                }
            }
            show()
        }
    }
}