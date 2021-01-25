package com.example.myyoutubever2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myyoutubever2.data.Video

class MainViewModel: ViewModel() {
    private val _playVideo = MutableLiveData<Video>()
    val playVideo: LiveData<Video> = _playVideo

    private val _playerViewHeight = MutableLiveData<Float>()
    val playerViewHeight: LiveData<Float> = _playerViewHeight

    fun startVideo(video: Video) {
        _playVideo.value = video
    }

    fun bottomTabAnimation(margin: Float) {
        _playerViewHeight.value = margin
    }
}