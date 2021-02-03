package com.example.myyoutubever2.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.example.myyoutubever2.database.entity.SubscribeDB
import io.reactivex.Maybe

@Dao
interface SubscribeDAO: BaseDAO<SubscribeDB> {
    @Query("SELECT * FROM SubscribeDB where userSeq = :seq")
    suspend fun getMySubscribeList(seq: Int): List<SubscribeDB>

}