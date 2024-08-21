package com.rizqanmr.core.data.network.model

import android.icu.text.SimpleDateFormat
import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

data class NewsNetwork(
    @SerializedName("description")
    val description: String = "",
    @SerializedName("time_created")
    val timeCreated: Long = 0,
    @SerializedName("rank")
    val rank: Int = 0,
    @SerializedName("banner_url")
    val bannerUrl: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("title")
    val title: String = ""
) {
    fun getReadableDate(): String {
        val currentTime = System.currentTimeMillis()

        val timeDifference = currentTime - timeCreated

        val minutesAgo = TimeUnit.MILLISECONDS.toMinutes(timeDifference)
        val hoursAgo = TimeUnit.MILLISECONDS.toHours(timeDifference)
        val daysAgo = TimeUnit.MILLISECONDS.toDays(timeDifference)
        val weeksAgo = daysAgo / 7
        val monthsAgo = weeksAgo / 4
        val yearsAgo = monthsAgo / 12

        return when {
            minutesAgo < 1 -> "Just now"
            minutesAgo < 60 -> "$minutesAgo mins ago"
            hoursAgo < 24 -> "$hoursAgo hours ago"
            daysAgo < 7 -> "$daysAgo days ago"
            weeksAgo < 4 -> "$weeksAgo weeks ago"
            monthsAgo < 12 -> "$monthsAgo months ago"
            yearsAgo < 1 -> "$yearsAgo year ago"
            else -> {
                val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                sdf.format(Date(timeCreated))
            }
        }
    }
}