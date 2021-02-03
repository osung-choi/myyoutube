package com.example.myyoutubever2.viewmodel

import androidx.lifecycle.*
import com.example.myyoutubever2.database.entity.SubscribeDB
import com.example.myyoutubever2.database.entity.UserDB
import com.example.myyoutubever2.database.entity.VideoDB
import com.example.myyoutubever2.database.repository.SubscribeRepository
import com.example.myyoutubever2.database.repository.UserRepository
import com.example.myyoutubever2.database.repository.VideoRepository
import io.reactivex.disposables.CompositeDisposable

class MainSubscribeViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val videoRepo = VideoRepository()
    private val userRepo = UserRepository()


    fun getMySubscribeUserList(seq: Int): LiveData<List<UserDB>> {
        return userRepo.getMySubscribeUserList(seq)
    }

    fun getSubscribeUserVideoList(seq: Int): LiveData<List<VideoDB>> {
        return videoRepo.getSubscribeUserVideoList(seq)
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}