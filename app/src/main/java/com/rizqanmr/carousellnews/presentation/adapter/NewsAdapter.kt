package com.rizqanmr.carousellnews.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rizqanmr.carousellnews.databinding.ItemNewsBinding
import com.rizqanmr.core.data.network.model.NewsNetwork
import com.rizqanmr.core.utils.setImageUrl

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private lateinit var newsListener: NewsListener

    private val diffUtil = object : DiffUtil.ItemCallback<NewsNetwork>() {
        override fun areItemsTheSame(oldItem: NewsNetwork, newItem: NewsNetwork): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NewsNetwork, newItem: NewsNetwork): Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun saveData(dataResponse: List<NewsNetwork>){
        asyncListDiffer.submitList(dataResponse)
    }

    fun setUserListener(newsListener: NewsListener) {
        this.newsListener = newsListener
    }

    class NewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("PrivateResource")
        fun bindData(news: NewsNetwork, listener: NewsListener) {
            binding.let { itemNews ->
                itemNews.item = news
                with(itemNews) {
                    ivNews.setImageUrl(news.bannerUrl, com.google.android.material.R.drawable.mtrl_ic_error)
                    cvNews.setOnClickListener { listener.onItemClick(itemNews, news) }
                }
            }
        }
    }

    interface NewsListener {
        fun onItemClick(itemNewsBinding: ItemNewsBinding, news: NewsNetwork?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val data = asyncListDiffer.currentList[position]
        if (data != null) {
            holder.bindData(data, newsListener)
        }
    }
}