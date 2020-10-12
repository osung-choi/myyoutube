package com.example.myyoutubever2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        playerView.initVideo("http://cache.midibus.kinxcdn.com/hls/ch_171e807a/173ff5638ea4fe1f/playlist.m3u8")

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