package com.example.myyoutubever2.data

import java.io.Serializable

/**
 * 비디오 정보
 */
data class Video(
    val seq: Int, //비디오 시퀀스
    val title: String, //비디오 타이틀
    val contents: String, //비디오 내용
    val thumbnailPath: String, //비디오 썸네일 경로
    val videoPath: String, //비디오 경로
    val likeCount: Int, //좋아요 수
    val replyCount: Int, //댓글 수
): Serializable