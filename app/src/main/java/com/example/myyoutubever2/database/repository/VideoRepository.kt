package com.example.myyoutubever2.database.repository

import android.app.Application
import com.example.myyoutubever2.database.AppDatabase
import com.example.myyoutubever2.database.entity.VideoDB
import io.reactivex.Maybe

class VideoRepository {
    private val database = AppDatabase.getInstance()
    private val dao = database.videoDao()

    fun selectVideoAll() : Maybe<List<VideoDB>> {
        return dao.selectVideoAll()
    }

    suspend fun getRecommendVideoList(seqList: List<Int>) : List<VideoDB> {
        return dao.getUserVideoList(seqList)
    }
}