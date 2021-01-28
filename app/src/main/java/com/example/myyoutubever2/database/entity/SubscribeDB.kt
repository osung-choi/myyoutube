package com.example.myyoutubever2.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey (
            entity = UserDB::class,
            parentColumns = arrayOf("userSeq"),
            childColumns = arrayOf("userSeq"),
            onDelete = ForeignKey.CASCADE
        ), ForeignKey(
            entity = UserDB::class,
            parentColumns = arrayOf("userSeq"),
            childColumns = arrayOf("userSubscribeSeq"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SubscribeDB(
    @PrimaryKey(autoGenerate = true) var seq: Int = 0,
    var userSeq: Int = 0,
    var userSubscribeSeq: Int = 0
)
