package com.example.myyoutubever2

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myyoutubever2.database.AppDatabase
import com.example.myyoutubever2.database.entity.SubscribeDB
import com.example.myyoutubever2.databinding.ActivityMainBinding
import com.example.myyoutubever2.fragment.MainSubscribeFragment
import com.example.myyoutubever2.fragment.MainVideoListFragment
import com.example.myyoutubever2.fragment.PlayerFragment
import com.example.myyoutubever2.viewmodel.MainViewModel
import com.example.myyoutubever2.viewmodel.PlayerFragViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val tabName = arrayListOf("홈","구독")

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

        viewModel.playerViewHeight.observe(this, {
            val layoutParams = binding.fragmentPlayer.layoutParams as FrameLayout.LayoutParams
            layoutParams.bottomMargin = it.toInt()
            binding.fragmentPlayer.layoutParams = layoutParams
        })

        viewModel.playVideoDB.observe(this, {
            val fragment = supportFragmentManager.findFragmentByTag(PLAYER_FRAGMENT_TAG)
            fragmentPlayer.visibility = View.VISIBLE

            if(fragment == null) {
                val playerFragment = PlayerFragment.newInstance(it)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentPlayer, playerFragment, PLAYER_FRAGMENT_TAG)
                    .commit()
            }else {
                val playerViewModel = ViewModelProvider(fragment)[PlayerFragViewModel::class.java]
                playerViewModel.setRecommendVideo(it)
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
            fragmentList.add(MainSubscribeFragment.newInstance()) //메인 구독 리스트
        }

        override fun getItemCount() = fragmentList.size
        override fun createFragment(position: Int) = fragmentList[position]
    }

    companion object {
        const val PLAYER_FRAGMENT_TAG = "tagPlayerFregment"
    }
}