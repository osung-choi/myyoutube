package com.example.myyoutubever2.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myyoutubever2.R
import com.example.myyoutubever2.adapter.MainSubscribeAdapter
import com.example.myyoutubever2.databinding.MainSubscribeFragmentBinding
import com.example.myyoutubever2.viewmodel.MainSubscribeViewModel
import com.example.myyoutubever2.viewmodel.MainViewModel

class MainSubscribeFragment : Fragment() {

    companion object {
        fun newInstance() = MainSubscribeFragment()
    }

    private lateinit var viewModel: MainSubscribeViewModel
    private lateinit var binding: MainSubscribeFragmentBinding
    private lateinit var mAdapter: MainSubscribeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_subscribe_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainSubscribeViewModel::class.java)

        initUi()
        initEvent()

        val mySeq = 0
        viewModel.getMySubscribeUserList(mySeq)
    }

    private fun initUi() {
        mAdapter = MainSubscribeAdapter(context!!)

        with(binding.mySubscribeList) {
            layoutManager = LinearLayoutManager(context)
            this.adapter = mAdapter
        }
    }

    private fun initEvent() {
        viewModel.subscribeUserList.observe(viewLifecycleOwner, {
            mAdapter.setSubscribeUserList(it)
        })

        viewModel.subscribeVideoList.observe(viewLifecycleOwner, {
            mAdapter.setVideoList(it)
        })

        mAdapter.setSubscribeUserClickListener {
            viewModel.getUserVideoList(it.userSeq)
        }

        mAdapter.setVideoClickListener {
            val mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
            mainViewModel.startVideo(it)
        }
    }
}