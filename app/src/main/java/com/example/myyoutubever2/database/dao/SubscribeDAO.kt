package com.example.myyoutubever2.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.myyoutubever2.database.entity.SubscribeDB

@Dao
interface SubscribeDAO {
    @Query("SELECT * FROM SubscribeDB where userSeq = :seq")
    suspend fun getMySubscribeList(seq: Int): List<SubscribeDB>
}