package com.example.myyoutubever2.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserDB(
    @PrimaryKey
    var userSeq: Int = 0,
    var nickname: String = "",
    var profileImg: String = ""
)
