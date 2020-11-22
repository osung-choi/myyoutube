package com.example.myyoutubever2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myyoutubever2.data.Video

class PlayerFragViewModel : ViewModel() {
    private val _videoData = MutableLiveData<Video>()
    val videoData : LiveData<Video> = _videoData

    fun setVideoData(video: Video) {
        _videoData.value = video
    }

}