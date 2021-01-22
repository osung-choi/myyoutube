package com.example.myyoutubever2

import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myyoutubever2.databinding.ActivityMainBinding
import com.example.myyoutubever2.fragment.MainVideoListFragment
import com.example.myyoutubever2.fragment.PlayerFragment
import com.example.myyoutubever2.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private val tabName = arrayListOf("홈","탐색","구독","보관함")

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        initUI()
        initEvent()
//        val sampleVideo = Utils.getSampleVideoData()
//        val playerFragment = PlayerFragment.newInstance(sampleVideo)
//        fragmentPlayer.visibility = View.VISIBLE
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragmentPlayer, playerFragment)
//            .commit()
    }

    private fun initUI() {
        binding.mainPager.adapter = ViewPagerAdapter(this)
        binding.mainPager.isUserInputEnabled = false

        TabLayoutMediator(binding.mainTab, binding.mainPager) { tab, position ->
            tab.text = tabName[position]
        }.attach()
    }

    private fun initEvent() {
        binding.mainTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let {
                    binding.mainPager.setCurrentItem(it, false)
                }
            }
        })
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentPlayer) as PlayerFragment?
        if(fragment != null) {
            if(!fragment.isFullScreen()) super.onBackPressed()

        }else {
            super.onBackPressed()
        }
    }

    private inner class ViewPagerAdapter(fa: FragmentActivity): FragmentStateAdapter(fa) {
        val fragmentList = arrayListOf<Fragment>()

        init {
            fragmentList.add(MainVideoListFragment.newInstance()) //메인 홈(영상 리스트)
            fragmentList.add(MainVideoListFragment.newInstance()) //메인 홈(영상 리스트)
            fragmentList.add(MainVideoListFragment.newInstance()) //메인 홈(영상 리스트)
            fragmentList.add(MainVideoListFragment.newInstance()) //메인 홈(영상 리스트)
        }

        override fun getItemCount() = fragmentList.size
        override fun createFragment(position: Int) = fragmentList[position]
    }
}