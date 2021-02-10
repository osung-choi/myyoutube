package com.example.myyoutubever2.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.myyoutubever2.R
import com.example.myyoutubever2.databinding.ViewVideoContentsBinding

class VideoContentsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val view =
        ViewVideoContentsBinding.inflate(LayoutInflater.from(context), this, true)

//    init {
//        attrs?.let {
//            val styleAttrs = context.obtainStyledAttributes(it, R.styleable.VideoContentsView)
//
//
//            view.videoContentsName.text =
//                styleAttrs.getString(R.styleable.VideoContentsView_contentsName)
//        }
//    }

//    fun setName(name: String) {
//        view.videoContentsName.text = name
//    }
//
//    fun setImage(@DrawableRes image: Int) {
//        Glide.with(context)
//            .load(image)
//            .centerCrop()
//            .into(view.videoContentsImage)
//    }
}
