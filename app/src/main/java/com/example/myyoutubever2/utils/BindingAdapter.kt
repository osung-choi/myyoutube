package com.example.myyoutubever2.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myyoutubever2.R
import com.example.myyoutubever2.adapter.MainVideoListAdapter
import com.example.myyoutubever2.adapter.VideoRecommendAdapter
import com.example.myyoutubever2.database.entity.VideoDB

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("app:circleImageSrc")
    fun setCircleImageSrc(imageView: ImageView, imageUrl: String) {
        Glide.with(imageView)
            .load(R.drawable.icon_default_profile)
            .circleCrop()
            .into(imageView)
    }
}