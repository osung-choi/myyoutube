package com.example.myyoutubever2.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.myyoutubever2.R
import com.example.myyoutubever2.data.Video
import com.example.myyoutubever2.utils.Utils
import kotlinx.android.synthetic.main.view_video_preview.view.*

class VideoPreview @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val mView = inflate(context, R.layout.view_video_preview, this)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec) //부모로부터 결정된 width
        mView.videoThumbnail.layoutParams.apply {
            height = Utils.getScreenHeightFromWidth(width)
        }
    }
    fun setVideoPreview(video: Video) {
        Glide.with(context)
            .load(video.thumbnailPath)
           .centerCrop()
            .into(mView.videoThumbnail)

        Glide.with(context)
            .load(R.mipmap.ic_launcher)
            .centerCrop()
            .into(mView.profileImage)

        mView.videoTitle.text = video.title

        //example
        mView.videoDuration.text = "11:45"
        mView.videoInformation.text = "팔배게 | 조회수 71만회 | 1개월전"

    }
}