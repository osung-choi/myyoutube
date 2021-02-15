package com.example.myyoutubever2.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * 비디오 정보
 */
@Entity(
    foreignKeys = [
        ForeignKey (
            entity = UserDB::class,
            parentColumns = arrayOf("userSeq"),
            childColumns = arrayOf("uploadUserSeq"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class VideoDB(
    @PrimaryKey(autoGenerate = true)
    var seq: Int = 0, //비디오 시퀀스
    var uploadUserSeq: Int = 0, //비디오 업로드한 유저 seq
    var title: String = "", //비디오 타이틀
    var contents: String = "", //비디오 내용
    var thumbnailPath: String = "", //비디오 썸네일 경로
    var videoPath: String = "", //비디오 경로
    var likeCount: Int = 0, //좋아요 수
    var notLikeCount: Int = 0, //싫어요 수
    var replyCount: Int = 0, //댓글 수
    var viewCount: Int = 0, //조회 수
    var uploadDate: Long = 0, //업로드 날짜
): Serializable