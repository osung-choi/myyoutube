package com.example.myyoutubever2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myyoutubever2.R
import com.example.myyoutubever2.adapter.VideoRecommendAdapter
import com.example.myyoutubever2.databinding.VideoRecommendFragmentBinding
import com.example.myyoutubever2.viewmodel.MainViewModel

class VideoRecommendFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding : VideoRecommendFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.video_recommend_fragment,
            container,
            false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.listVideoRecommend.layoutManager = LinearLayoutManager(context)
        binding.listVideoRecommend.adapter = VideoRecommendAdapter()

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        viewModel.playVideo.observe(viewLifecycleOwner, {
            val adapter = binding.listVideoRecommend.adapter as VideoRecommendAdapter
            adapter.setPlayVideo(it)

            binding.loading.visibility = View.VISIBLE
            binding.listVideoRecommend.visibility = View.GONE
            binding.listVideoRecommend.scrollToPosition(0)
        })

        viewModel.recommendVideoData.observe(viewLifecycleOwner, {
            val adapter = binding.listVideoRecommend.adapter as VideoRecommendAdapter
            adapter.setRecommendVideoList(it)

            binding.loading.visibility = View.GONE
            binding.listVideoRecommend.visibility = View.VISIBLE
        })

        (binding.listVideoRecommend.adapter as VideoRecommendAdapter).setRecommendVideoClickListener {
            viewModel.startVideo(it)
        }
    }

    companion object {
        fun newInstance() = VideoRecommendFragment()
    }

}