package com.example.myyoutubever2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myyoutubever2.database.entity.VideoDB
import com.example.myyoutubever2.database.repository.VideoRepository
import kotlinx.coroutines.launch

class MainVideoListViewModel: ViewModel() {
    private val videoDB = VideoRepository()

    private val _allVideoList = MutableLiveData<List<VideoDB>>()
    val allVideoList: LiveData<List<VideoDB>> = _allVideoList

    fun setRecommendVideoList() {
        viewModelScope.launch {
            _allVideoList.value = videoDB.selectVideoAll()
        }
    }
}