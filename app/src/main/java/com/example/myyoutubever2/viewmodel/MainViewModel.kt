package com.example.myyoutubever2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myyoutubever2.database.entity.VideoDB
import com.example.myyoutubever2.database.repository.VideoRepository
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val videoRepo = VideoRepository()

    private val _playVideo = MutableLiveData<VideoDB>()
    val playVideo: LiveData<VideoDB> = _playVideo

    private val _recommendVideoData = MutableLiveData<List<VideoDB>>()
    val recommendVideoData: LiveData<List<VideoDB>> = _recommendVideoData

    private val _playerViewHeight = MutableLiveData<Float>()
    val playerViewHeight: LiveData<Float> = _playerViewHeight

    fun startVideo(videoDB: VideoDB) {
        _playVideo.value = videoDB

        val recommendSeq = getRecommendUserSeq(videoDB.uploadUserSeq)
        viewModelScope.launch {
            _recommendVideoData.value = videoRepo.getUserVideoList(recommendSeq)
        }
    }

    fun bottomTabAnimation(margin: Float) {
        _playerViewHeight.value = margin
    }

    private fun getRecommendUserSeq(userSeq: Int): List<Int> {
        return listOf(
            (userSeq + 1) % 21,
            (userSeq + 2) % 21,
            (userSeq + 3) % 21
        )
    }

}