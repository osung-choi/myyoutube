package com.example.myyoutubever2.database.repository

import android.app.Application
import com.example.myyoutubever2.database.AppDatabase
import com.example.myyoutubever2.database.entity.SubscribeDB

class SubscribeRepository() {
    private val database = AppDatabase.getInstance()
    private val dao = database.subscribeDao()

    suspend fun getMySubscribeList(seq: Int) : List<SubscribeDB> {
        return dao.getMySubscribeList(seq)
    }
}