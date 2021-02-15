package com.example.myyoutubever2.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.myyoutubever2.database.entity.VideoDB
import com.example.myyoutubever2.database.entity.VideoData

@Dao
interface VideoDAO: BaseDAO<VideoDB> {
    @Query("SELECT * FROM VideoDB")
    suspend fun selectVideoAll(): List<VideoDB>

    @Query("SELECT * FROM VideoDB WHERE uploadUserSeq IN (:seqList)")
    suspend fun getUserVideoList(seqList: List<Int>) : List<VideoDB>

    @Query("SELECT video.* FROM SubscribeDB AS subscribe LEFT JOIN VideoDB AS video ON subscribe.userSubscribeSeq = video.uploadUserSeq WHERE subscribe.userSeq = :seq")
    suspend fun getSubscribeUserVideoList(seq: Int): List<VideoDB>
}