package com.example.myyoutubever2.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myyoutubever2.databinding.ViewVideoUserBinding

class VideoUserInfoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        ViewVideoUserBinding.inflate(LayoutInflater.from(context), this, true)
    }
}