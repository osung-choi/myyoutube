package com.example.myyoutubever2.database.repository

import androidx.lifecycle.LiveData
import com.example.myyoutubever2.database.AppDatabase
import com.example.myyoutubever2.database.entity.UserDB

class UserRepository {
    private val database = AppDatabase.getInstance()
    private val dao = database.userDao()

    fun getMySubscribeUserList(seq: Int): LiveData<List<UserDB>> {
        return dao.getMySubscribeUserList(seq)
    }
}