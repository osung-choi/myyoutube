package com.example.myyoutubever2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myyoutubever2.data.RecommendVideo
import com.example.myyoutubever2.data.Video
import com.example.myyoutubever2.utils.Utils

class MainVideoListViewModel : ViewModel() {
    private val _recommendVideos = MutableLiveData<RecommendVideo>()
    val recommendVideos : LiveData<RecommendVideo> = _recommendVideos

    fun setRecommendVideoList() {
        _recommendVideos.value = Utils.getRecommendVideoData()
    }
}