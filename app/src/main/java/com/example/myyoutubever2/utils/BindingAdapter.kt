package com.example.myyoutubever2.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myyoutubever2.R
import com.example.myyoutubever2.adapter.MainVideoListAdapter
import com.example.myyoutubever2.adapter.VideoRecommendAdapter
import com.example.myyoutubever2.database.entity.VideoDB
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
    @BindingAdapter(value = ["app:contentsImage", "app:contentsName"], requireAll = false)
    fun setVideoContentView(view: VideoContentsView, contentsImage: String, contentsName: String) {
        view.videoContentsName.text = contentsName

//        Glide.with(view)
//            .load(image)
//            .centerCrop()
//            .into(view.videoContentsImage)
    }
}
//
//fun setName(name: String) {
//    view.videoContentsName.text = name
//}
//
//fun setImage(@DrawableRes image: Int) {
//    Glide.with(context)
//        .load(image)
//        .centerCrop()
//        .into(view.videoContentsImage)
//}