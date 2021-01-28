package com.example.myyoutubever2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myyoutubever2.database.entity.VideoDB

class MainViewModel: ViewModel() {
    private val _playVideo = MutableLiveData<VideoDB>()
    val playVideoDB: LiveData<VideoDB> = _playVideo

    private val _playerViewHeight = MutableLiveData<Float>()
    val playerViewHeight: LiveData<Float> = _playerViewHeight

    fun startVideo(videoDB: VideoDB) {
        _playVideo.value = videoDB
    }

    fun bottomTabAnimation(margin: Float) {
        _playerViewHeight.value = margin
    }
}