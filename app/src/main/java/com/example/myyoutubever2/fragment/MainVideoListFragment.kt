package com.example.myyoutubever2.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myyoutubever2.R
import com.example.myyoutubever2.adapter.MainVideoListAdapter
import com.example.myyoutubever2.databinding.MainVideoListFragmentBinding
import com.example.myyoutubever2.viewmodel.MainVideoListViewModel
import com.example.myyoutubever2.viewmodel.MainViewModel

class MainVideoListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        fun newInstance() = MainVideoListFragment()
    }

    private lateinit var viewModel: MainVideoListViewModel
    private lateinit var binding: MainVideoListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_video_list_fragment, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainVideoListViewModel::class.java]

        binding.viewModel = viewModel

        binding.swipeRefresh.setOnRefreshListener(this)

        binding.mainVideoList.layoutManager = LinearLayoutManager(context)
        binding.mainVideoList.adapter = MainVideoListAdapter {
            val mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
            mainViewModel.startVideo(it)
        }

        viewModel.allVideoList.observe(viewLifecycleOwner, {
            val adapter = binding.mainVideoList.adapter as MainVideoListAdapter
            adapter.setRecommendVideoList(it)

            binding.swipeRefresh.isRefreshing = false
        })

        viewModel.setRecommendVideoList()
    }

    override fun onRefresh() {
        viewModel.setRecommendVideoList()
    }
}