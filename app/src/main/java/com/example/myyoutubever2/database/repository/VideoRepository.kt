package com.example.myyoutubever2.database.repository

import androidx.lifecycle.LiveData
import com.example.myyoutubever2.database.AppDatabase
import com.example.myyoutubever2.database.entity.VideoDB

class VideoRepository {
    private val database = AppDatabase.getInstance()
    private val dao = database.videoDao()

    fun selectVideoAll() : LiveData<List<VideoDB>> {
        return dao.selectVideoAll()
    }

    suspend fun getRecommendVideoList(seqList: List<Int>) : List<VideoDB> {
        return dao.getUserVideoList(seqList)
    }
}