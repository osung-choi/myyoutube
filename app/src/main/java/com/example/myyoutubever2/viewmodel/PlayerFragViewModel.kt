package com.example.myyoutubever2.viewmodel

import androidx.lifecycle.*
import com.example.myyoutubever2.database.entity.VideoDB
import com.example.myyoutubever2.database.repository.VideoRepository
import kotlinx.coroutines.launch

class PlayerFragViewModel : ViewModel() {
    private val videoRepo = VideoRepository()

    private val _videoData = MutableLiveData<VideoDB>()
    val videoDBData : LiveData<VideoDB> = _videoData

    private val _recommendVideoData = MutableLiveData<List<VideoDB>>()
    val recommendVideoData : LiveData<List<VideoDB>> = _recommendVideoData

    fun setVideoData(videoDB: VideoDB) {
        _videoData.value = videoDB

        val userSeq = videoDB.userSeq

        val recommendSeq = getRecommendUserSeq(userSeq)

        viewModelScope.launch {
            _recommendVideoData.value = videoRepo.getRecommendVideoList(recommendSeq)
        }
    }

    private fun getRecommendUserSeq(userSeq: Int): List<Int> {
        return listOf((userSeq + 1) % 21,
            (userSeq + 2) % 21,
            (userSeq + 3) % 21)
    }
}