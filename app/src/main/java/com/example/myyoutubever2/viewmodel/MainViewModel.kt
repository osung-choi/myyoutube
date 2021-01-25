package com.example.myyoutubever2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myyoutubever2.data.Video

class MainViewModel: ViewModel() {
    private val _playVideo = MutableLiveData<Video>()
    val playVideo: LiveData<Video> = _playVideo

    fun startVideo(video: Video) {
        _playVideo.value = video
    }
}