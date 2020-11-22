package com.example.myyoutubever2.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myyoutubever2.R
import com.example.myyoutubever2.viewmodel.PlayerFuncViewModel

class PlayerFuncFragment : Fragment() {

    companion object {
        fun newInstance() = PlayerFuncFragment()
    }

    private lateinit var viewModel: PlayerFuncViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.player_func_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PlayerFuncViewModel::class.java)
        // TODO: Use the ViewModel
    }

}