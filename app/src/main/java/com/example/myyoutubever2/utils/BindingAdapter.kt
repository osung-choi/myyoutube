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
    @BindingAdapter("tools:videoRecommendItem")
    fun setVideoRecommendItem(recyclerView: RecyclerView, items: List<VideoDB>?) {
        items?.let { list ->
            (recyclerView.adapter as? VideoRecommendAdapter)?.let {
                it.setRecommendVideoList(list)
                it.notifyDataSetChanged()
            }

            (recyclerView.adapter as? MainVideoListAdapter)?.let {
                it.setRecommendVideoList(list)
                it.notifyDataSetChanged()
            }
        }
    }

    @JvmStatic
    @BindingAdapter("app:circleImageSrc")
    fun setCircleImageSrc(imageView: ImageView, imageUrl: String) {
        Glide.with(imageView)
            .load(R.drawable.icon_default_profile)
            .circleCrop()
            .into(imageView)
    }
}