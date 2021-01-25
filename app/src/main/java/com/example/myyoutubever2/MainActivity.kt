package com.example.myyoutubever2

import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myyoutubever2.databinding.ActivityMainBinding
import com.example.myyoutubever2.fragment.MainVideoListFragment
import com.example.myyoutubever2.fragment.PlayerFragment
import com.example.myyoutubever2.utils.Utils
import com.example.myyoutubever2.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val tabName = arrayListOf("홈","탐색","구독","보관함")

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel

        initUI()
        initEvent()
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
                    binding.appbar.setExpanded(true, true) //AppBar 영역을 동적으로 show/hide 하는 함수.
                }
            }
        })

        viewModel.playVideo.observe(this, {
            val playerFragment = PlayerFragment.newInstance(it)
            fragmentPlayer.visibility = View.VISIBLE
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentPlayer, playerFragment)
                .commit()
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