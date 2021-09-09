package com.example.moviesapptask.utilities.extensions.imageview

import android.widget.ImageView
import com.bumptech.glide.Glide

enum class ImageViewMode {
    NORMAL
}

fun ImageView.load(url: String, mode: ImageViewMode = ImageViewMode.NORMAL) {
    val load = Glide.with(context).load(url)
    if (mode == ImageViewMode.NORMAL) load.into(this)
}