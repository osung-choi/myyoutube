package com.example.myyoutubever2

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.myyoutubever2.databinding.ActivityMainBinding
import com.example.myyoutubever2.fragment.PlayerFragment
import com.example.myyoutubever2.utils.Utils
import com.example.myyoutubever2.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.viewModel.getVideoList()

        val sampleVideo = Utils.getSampleVideoData()
        val playerFragment = PlayerFragment.newInstance(sampleVideo)
        fragmentPlayer.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentPlayer, playerFragment)
            .commit()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentPlayer) as PlayerFragment?
        if(fragment != null) {
            if(!fragment.isFullScreen()) super.onBackPressed()

        }else {
            super.onBackPressed()
        }
    }
}