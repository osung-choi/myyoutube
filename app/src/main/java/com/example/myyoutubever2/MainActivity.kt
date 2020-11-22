package com.example.myyoutubever2

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myyoutubever2.fragment.PlayerFragment
import com.example.myyoutubever2.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        click.setOnClickListener {
            val sampleVideo = Utils.getSampleVideoData()
            val playerFragment = PlayerFragment.newInstance(sampleVideo)
            fragmentPlayer.visibility = View.VISIBLE
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentPlayer, playerFragment)
                .commit()
        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentPlayer) as PlayerFragment?
        if(fragment != null) {
            if(!fragment.isFullScreen()) super.onBackPressed()

        }else {
            super.onBackPressed()
        }

//        if(!playerLayout.isFullScreen()) {
//            super.onBackPressed()
//        }
    }
}