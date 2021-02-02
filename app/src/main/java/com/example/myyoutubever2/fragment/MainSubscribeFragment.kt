package com.example.myyoutubever2.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.myyoutubever2.R
import com.example.myyoutubever2.viewmodel.MainSubscribeViewModel

class MainSubscribeFragment : Fragment() {

    companion object {
        fun newInstance() = MainSubscribeFragment()
    }

    private lateinit var viewModel: MainSubscribeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_subscribe_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainSubscribeViewModel::class.java)

        initEvent()
    }

    private fun initEvent() {
        val mySeq = 0
        viewModel.getMySubscribeList(mySeq).observe(viewLifecycleOwner, {
            Log.d("tag", "${it}")
        })

        viewModel.getSubscribeUserVideoList(mySeq).observe(viewLifecycleOwner, {
            Log.d("tag", "${it}")
        })
    }
}