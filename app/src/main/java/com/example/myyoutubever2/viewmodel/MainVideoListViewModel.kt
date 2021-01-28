package com.example.myyoutubever2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myyoutubever2.database.entity.VideoDB
import com.example.myyoutubever2.database.repository.VideoRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainVideoListViewModel: ViewModel() {
    private val videoDB = VideoRepository()

    private val compositeDisposable = CompositeDisposable()

    private val _recommendVideos = MutableLiveData<List<VideoDB>>()
    val recommendVideos : LiveData<List<VideoDB>> = _recommendVideos

    fun setRecommendVideoList() {
        val disposable = videoDB.selectVideoAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _recommendVideos.value = it
            }

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.dispose()
    }
}