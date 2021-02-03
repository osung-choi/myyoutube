package com.example.myyoutubever2.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myyoutubever2.database.entity.UserDB

@Dao
interface UserDAO: BaseDAO<UserDB> {
    @Query("select user.* from UserDB as user left join SubscribeDB as subscribe on user.userSeq = subscribe.userSubscribeSeq where subscribe.userSeq = :seq")
    fun getMySubscribeUserList(seq: Int): LiveData<List<UserDB>>
}