package com.example.myyoutubever2.viewmodel

import androidx.lifecycle.*
import com.example.myyoutubever2.database.entity.VideoDB
import com.example.myyoutubever2.database.repository.VideoRepository
import kotlinx.coroutines.launch

class PlayerFragViewModel : ViewModel() {
    private val videoRepo = VideoRepository()

    private val _recommendVideoData = MutableLiveData<List<VideoDB>>()
    val recommendVideoData : LiveData<List<VideoDB>> = _recommendVideoData

    fun setRecommendVideo(videoUserSeq: Int) {
        val recommendSeq = getRecommendUserSeq(videoUserSeq)

        viewModelScope.launch {
            _recommendVideoData.value = videoRepo.getUserVideoList(recommendSeq)
        }
    }

    private fun getRecommendUserSeq(userSeq: Int): List<Int> {
        return listOf((userSeq + 1) % 21,
            (userSeq + 2) % 21,
            (userSeq + 3) % 21)
    }
}