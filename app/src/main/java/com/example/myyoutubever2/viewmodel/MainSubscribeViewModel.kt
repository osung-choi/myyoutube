package com.example.myyoutubever2.viewmodel

import androidx.lifecycle.*
import com.example.myyoutubever2.database.entity.SubscribeDB
import com.example.myyoutubever2.database.entity.UserDB
import com.example.myyoutubever2.database.entity.VideoDB
import com.example.myyoutubever2.database.repository.SubscribeRepository
import com.example.myyoutubever2.database.repository.UserRepository
import com.example.myyoutubever2.database.repository.VideoRepository
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class MainSubscribeViewModel : ViewModel() {
    private val videoRepo = VideoRepository()
    private val userRepo = UserRepository()

    private val _subscribeUserList = MutableLiveData<List<UserDB>>()
    val subscribeUserList: LiveData<List<UserDB>> = _subscribeUserList

    private val _subscribeVideoList = MutableLiveData<List<VideoDB>>()
    val subscribeVideoList: LiveData<List<VideoDB>> = _subscribeVideoList

    fun getMySubscribeUserList(seq: Int) {
        viewModelScope.launch {
            _subscribeUserList.value = userRepo.getMySubscribeUserList(seq)
            _subscribeVideoList.value = videoRepo.getSubscribeUserVideoList(seq)
        }
    }

    fun getUserVideoList(userSeq: Int) {
        viewModelScope.launch {
            _subscribeVideoList.value = videoRepo.getUserVideoList(listOf(userSeq))
        }
    }
}