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
    init {
        ViewVideoContentsBinding.inflate(LayoutInflater.from(context), this, true)
    }
}
