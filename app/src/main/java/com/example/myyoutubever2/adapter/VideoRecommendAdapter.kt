package com.example.myyoutubever2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myyoutubever2.R
import com.example.myyoutubever2.data.Video
import kotlinx.android.synthetic.main.adapter_video_recommend.view.*

class VideoRecommendAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = arrayListOf<Video>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            videoInformationType -> VideoInformationViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.adapter_video_information, parent, false)
            )

            else -> VideoRecommendViewHolder (
                LayoutInflater.from(parent.context).inflate(R.layout.adapter_video_recommend, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder as? VideoInformationViewHolder != null) {
            holder.bind()
        }else if(holder as? VideoRecommendViewHolder != null) {
            holder.bind(items[position-1])
        }

    }

    override fun getItemCount(): Int {
        return items.size + 1
    }

    override fun getItemViewType(position: Int)
            = if(position == 0) videoInformationType else videoRecommendType

    fun setRecommendVideoList(recommendVideo: ArrayList<Video>) {
        items.addAll(recommendVideo)
    }

    inner class VideoInformationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {

        }
    }

    inner class VideoRecommendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(video: Video) {
            itemView.videoPreview.setVideoPreview(video)
        }
    }

    companion object {
        const val videoInformationType = 1
        const val videoRecommendType = 2
    }
}