package com.example.myyoutubever2.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myyoutubever2.R
import com.example.myyoutubever2.adapter.VideoRecommendAdapter
import com.example.myyoutubever2.data.Video
import com.example.myyoutubever2.databinding.VideoRecommendFragmentBinding
import com.example.myyoutubever2.viewmodel.PlayerFragViewModel

class VideoRecommendFragment : Fragment() {

    private lateinit var viewModel: PlayerFragViewModel
    private lateinit var binding : VideoRecommendFragmentBinding
//    private lateinit var video: Video

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        arguments?.let {
//            video = it.getSerializable(PARAM_VIDEO) as Video
//        }
    }
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
        viewModel = ViewModelProvider(parentFragment!!).get(PlayerFragViewModel::class.java)
        binding.viewModel = viewModel

        binding.listVideoRecommend.layoutManager = LinearLayoutManager(context)
        binding.listVideoRecommend.adapter = VideoRecommendAdapter()

        viewModel.videoData.observe(viewLifecycleOwner) {
            Log.d("asd","asd")
        }
    }

    companion object {
//        private const val PARAM_VIDEO = "param_video"
//
//        fun newInstance(video: Video) = VideoRecommendFragment().apply {
//            arguments = Bundle().apply {
//                putSerializable(PARAM_VIDEO, video)
//            }
//        }

        fun newInstance() = VideoRecommendFragment()
    }

}