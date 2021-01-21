package com.example.myyoutubever2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myyoutubever2.data.RecommendVideo
import com.example.myyoutubever2.data.Video
import com.example.myyoutubever2.utils.Utils

class PlayerFragViewModel : ViewModel() {
    private val _videoData = MutableLiveData<Video>()
    val videoData : LiveData<Video> = _videoData

    private val _recommendVideoData = MutableLiveData<RecommendVideo>()
    val recommendVideoData : LiveData<RecommendVideo> = _recommendVideoData

    fun setVideoData(video: Video) {
        _videoData.value = video

        //추후 video seq에 따른 추천 비디오 리스트 받아오는 로직 구현해보기.
        _recommendVideoData.value = Utils.getRecommendVideoData()
    }

}