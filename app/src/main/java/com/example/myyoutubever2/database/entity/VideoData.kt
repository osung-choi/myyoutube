package com.example.myyoutubever2.database.entity

import androidx.room.Embedded

data class VideoData(
    @Embedded var video: VideoDB,
    @Embedded var user: UserDB
)