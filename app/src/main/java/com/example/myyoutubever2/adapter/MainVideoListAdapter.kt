package com.example.myyoutubever2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myyoutubever2.R
import com.example.myyoutubever2.data.Video
import kotlinx.android.synthetic.main.adapter_video_recommend.view.*

class MainVideoListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = arrayListOf<Video>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = MainVideoListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_video_recommend, parent, false))


    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is MainVideoListViewHolder) {
            holder.bind(items[position])
        }
    }

    fun setRecommendVideoList(videoList: ArrayList<Video>) {
        items.clear()
        items.addAll(videoList)
    }

    private inner class MainVideoListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(video: Video) {
            itemView.videoPreview.setVideoPreview(video)
        }
    }
}