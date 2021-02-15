package com.example.myyoutubever2.database.repository

import com.example.myyoutubever2.database.AppDatabase
import com.example.myyoutubever2.database.entity.VideoDB

class VideoRepository {
    private val database = AppDatabase.getInstance()
    private val dao = database.videoDao()

    suspend fun selectVideoAll() : List<VideoDB> {
        return dao.selectVideoAll()
    }

    suspend fun getUserVideoList(seqList: List<Int>) : List<VideoDB> {
        return dao.getUserVideoList(seqList)
    }

    suspend fun getSubscribeUserVideoList(seq: Int) : List<VideoDB> {
        return dao.getSubscribeUserVideoList(seq)
    }
}