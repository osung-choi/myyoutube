package com.example.myyoutubever2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myyoutubever2.R
import com.example.myyoutubever2.database.entity.UserDB
import com.example.myyoutubever2.database.entity.VideoDB
import com.example.myyoutubever2.databinding.AdapterMainSubscribeHeaderBinding
import kotlinx.android.synthetic.main.adapter_video_recommend.view.*

class MainSubscribeAdapter(val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val subscribeUserList = arrayListOf<UserDB>()
    private val videoList = arrayListOf<VideoDB>()

    private var subscribeUserClick: ((UserDB) -> Unit)? = null
    private var videoClick: ((VideoDB) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_main_subscribe_header, parent, false))
            else -> VideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_video_recommend, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is HeaderViewHolder) {
            holder.bind(subscribeUserList)
        } else if(holder is VideoViewHolder) {
            holder.bind(videoList[position - 1])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            0 -> VIEW_TYPE_HEADER
            else -> VIEW_TYPE_VIDEO
        }
    }

    override fun getItemCount(): Int {
        return videoList.size + 1
    }

    fun setVideoClickListener(listener: (VideoDB) -> Unit) {
        videoClick = listener
    }

    fun setSubscribeUserClickListener(listener: (UserDB) -> Unit) {
        subscribeUserClick = listener
    }

    fun setSubscribeUserList(items: List<UserDB>) {
        subscribeUserList.clear()
        subscribeUserList.addAll(items)
        notifyItemChanged(0)
    }

    fun setVideoList(items: List<VideoDB>) {
        videoList.clear()
        videoList.addAll(items)
        notifyDataSetChanged()
    }


    inner class HeaderViewHolder(private val binding: AdapterMainSubscribeHeaderBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(subscribeUserList: ArrayList<UserDB>) {
            with(binding) {
                val manager = LinearLayoutManager(context)
                manager.orientation = LinearLayoutManager.HORIZONTAL

                val adapter = SubscribeHeaderAdapter(subscribeUserClick)

                listMySubscribe.layoutManager = manager
                listMySubscribe.adapter = adapter
                adapter.setUserList(subscribeUserList)
            }
        }
    }

    inner class VideoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(videoDB: VideoDB) {
            itemView.videoPreview.setVideoPreview(videoDB)

            itemView.setOnClickListener {
                videoClick?.invoke(videoDB)
            }
        }
    }

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_VIDEO = 1
    }
}