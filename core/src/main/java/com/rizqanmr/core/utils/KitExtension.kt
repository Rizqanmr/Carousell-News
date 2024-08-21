package com.rizqanmr.core.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.setImageUrl(url: String?, placeholder: Int?= null) {
    if (url.isNullOrBlank() && (placeholder == null || placeholder == 0)) return

    val glideRequest = Glide.with(context).load(url).centerCrop()
    placeholder?.let {
        glideRequest.placeholder(it)
    }
    glideRequest.into(this)
}