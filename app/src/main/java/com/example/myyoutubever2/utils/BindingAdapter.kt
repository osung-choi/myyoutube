package com.example.myyoutubever2.utils

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myyoutubever2.adapter.VideoRecommendAdapter
import com.example.myyoutubever2.data.Video

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("tools:videoRecommendItem")
    fun setVideoRecommendItem(recyclerView: RecyclerView, item: Video) {
        (recyclerView.adapter as? VideoRecommendAdapter)?.let {
            it.setRecommendVideoList(item.recommendVideo)
            it.notifyDataSetChanged()
        }
    }
}