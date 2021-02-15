package com.example.myyoutubever2.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.myyoutubever2.R
import com.example.myyoutubever2.view.VideoContentsView
import kotlinx.android.synthetic.main.view_video_contents.view.*

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("app:circleImageSrc")
    fun setCircleImageSrc(imageView: ImageView, imageUrl: String) {
        Glide.with(imageView)
            .load(R.drawable.icon_default_profile)
            .circleCrop()
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter(value = ["app:likeCount","app:notLikeCount"], requireAll = false)
    fun setVideoContentView(view: VideoContentsView, likeCount: Int, notLikeCount: Int) {
        view.videoLikeCount.text = Utils.getSnsCount(likeCount)
        view.videoNotLikeCount.text = Utils.getSnsCount(notLikeCount)
    }

    @JvmStatic
    @BindingAdapter(value = ["app:viewCount","app:uploadDate"], requireAll = false)
    fun setVideoViewCount(view: TextView, viewCount: Int, uploadDate: Long) {
        view.text = "조회수 ${Utils.getSnsViewCount(viewCount)} . ${Utils.getSnsUploadDate(uploadDate)}"
    }
}
