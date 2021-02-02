package com.example.myyoutubever2.database.dao

import androidx.room.*

@Dao
interface BaseDAO<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg data: T)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(data: T)

    @Delete
    suspend fun delete(data: T)
}