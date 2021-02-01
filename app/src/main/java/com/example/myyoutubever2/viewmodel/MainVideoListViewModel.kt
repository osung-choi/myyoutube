package com.example.myyoutubever2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myyoutubever2.database.entity.VideoDB
import com.example.myyoutubever2.database.repository.VideoRepository

class MainVideoListViewModel: ViewModel() {
    private val videoDB = VideoRepository()

    fun setRecommendVideoList(): LiveData<List<VideoDB>> {
        return videoDB.selectVideoAll()
    }
}