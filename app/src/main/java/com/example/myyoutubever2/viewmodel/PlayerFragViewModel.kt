package com.example.myyoutubever2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myyoutubever2.database.entity.VideoDB
import com.example.myyoutubever2.database.repository.VideoRepository
import kotlinx.coroutines.launch

class PlayerFragViewModel : ViewModel() {
    private val videoRepo = VideoRepository()

    private val _playVideo = MutableLiveData<VideoDB>()
    val playVideo: LiveData<VideoDB> = _playVideo


    private val _fullLayout = MutableLiveData<Boolean>()
    val fullLayout: LiveData<Boolean> = _fullLayout

    private val _recommendVideoData = MutableLiveData<List<VideoDB>>()
    val recommendVideoData: LiveData<List<VideoDB>> = _recommendVideoData

    fun setRecommendVideo(video: VideoDB) {
        val value = _playVideo.value
        if(value != null && value.seq == video.seq) {
            _fullLayout.value = true
        }else {
            _playVideo.value = video

            val recommendSeq = getRecommendUserSeq(video.uploadUserSeq)
            viewModelScope.launch {
                _recommendVideoData.value = videoRepo.getUserVideoList(recommendSeq)
            }
        }
    }

    private fun getRecommendUserSeq(userSeq: Int): List<Int> {
        return listOf(
            (userSeq + 1) % 21,
            (userSeq + 2) % 21,
            (userSeq + 3) % 21
        )
    }
}