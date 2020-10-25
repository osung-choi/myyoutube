package com.example.myyoutubever2

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycle.addObserver(playerLayout)
        click.setOnClickListener {
            playerLayout.startVideo()
        }
    }

    private fun showStatusBar() {
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }

    private fun doFullScreen() {
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        when(newConfig.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> { //세로 전환
                showStatusBar()
                playerLayout.setPortraitView()
            }
            Configuration.ORIENTATION_LANDSCAPE -> { //가로 전환
                doFullScreen()
                playerLayout.setLandscapeView()
            }
        }
    }

    override fun onBackPressed() {
        if(!playerLayout.isFullScreen()) {
            super.onBackPressed()
        }
    }
}