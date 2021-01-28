package com.example.myyoutubever2.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.myyoutubever2.database.entity.VideoDB
import io.reactivex.Maybe

@Dao
interface VideoDAO {
    @Query("SELECT * FROM VideoDB")
    fun selectVideoAll(): Maybe<List<VideoDB>>

    @Query("SELECT * FROM VideoDB WHERE userSeq IN (:seqList)")
    suspend fun getUserVideoList(seqList: List<Int>) : List<VideoDB>
}