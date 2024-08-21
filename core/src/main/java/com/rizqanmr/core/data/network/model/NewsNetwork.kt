package com.rizqanmr.core.data.network.model

import com.google.gson.annotations.SerializedName

data class NewsNetwork(
    @SerializedName("description")
    val description: String = "",
    @SerializedName("time_created")
    val timeCreated: Int = 0,
    @SerializedName("rank")
    val rank: Int = 0,
    @SerializedName("banner_url")
    val bannerUrl: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("title")
    val title: String = ""
)