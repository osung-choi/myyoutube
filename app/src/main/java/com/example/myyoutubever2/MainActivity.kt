package com.example.myyoutubever2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        with(testVideoPlayer.layoutParams) {
            width = 385
            height = 216
        }

        click.setOnClickListener {
            playerLayout.startVideo()
        }
    }

    override fun onBackPressed() {
        if(!playerLayout.isFullScreen()) {
            super.onBackPressed()
        }
    }
}