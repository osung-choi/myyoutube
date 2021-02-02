package com.example.myyoutubever2.database.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.myyoutubever2.database.AppDatabase
import com.example.myyoutubever2.database.entity.SubscribeDB
import io.reactivex.Maybe

class SubscribeRepository {
    private val database = AppDatabase.getInstance()
    private val dao = database.subscribeDao()

    fun getMySubscribeList(seq: Int) : LiveData<List<SubscribeDB>> {
        return dao.getMySubscribeList(seq)
    }
}