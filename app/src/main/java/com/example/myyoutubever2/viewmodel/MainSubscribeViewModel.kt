package com.example.myyoutubever2.viewmodel

import androidx.lifecycle.*
import com.example.myyoutubever2.database.entity.SubscribeDB
import com.example.myyoutubever2.database.entity.VideoDB
import com.example.myyoutubever2.database.repository.SubscribeRepository
import com.example.myyoutubever2.database.repository.VideoRepository
import io.reactivex.disposables.CompositeDisposable

class MainSubscribeViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val subscribeRepo = SubscribeRepository()
    private val videoRepo = VideoRepository()


    fun getMySubscribeList(seq: Int): LiveData<List<SubscribeDB>> {
        return subscribeRepo.getMySubscribeList(seq)
    }

    fun getSubscribeUserVideoList(seq: Int): LiveData<List<VideoDB>> {
        return videoRepo.getSubscribeUserVideoList(seq)
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}