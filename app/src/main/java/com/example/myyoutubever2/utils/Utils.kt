package com.example.myyoutubever2.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics
import android.util.TypedValue
import com.example.myyoutubever2.database.entity.SubscribeDB
import com.example.myyoutubever2.database.entity.VideoDB


object Utils {
    fun getScreenHeightFromWidth(width: Int) = width * 225 / 400

    fun convertDpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    fun getOrientation(context: Context) = context.resources.configuration.orientation


    fun getDisplayWidth(context: Context) : Int {
        return if(getOrientation(context) == Configuration.ORIENTATION_PORTRAIT) {
            context.resources.displayMetrics.widthPixels
        }else {
            context.resources.displayMetrics.widthPixels + getNavigationBarWidth(context)
        }
    }

    fun getDisplayHeight(context: Context) : Int {
        return context.resources.displayMetrics.heightPixels
//        return if(getOrientation(context) == Configuration.ORIENTATION_PORTRAIT) {
//            context.resources.displayMetrics.heightPixels //- getStatusBarHeight(context)
//        }else {
//            context.resources.displayMetrics.heightPixels
//        }
    }

    fun getSubscribeSampleData() : List<SubscribeDB> {
        return listOf(SubscribeDB(0,1,2),
            SubscribeDB(0,1,4),
            SubscribeDB(0,1,5),
            SubscribeDB(0,1,7),
            SubscribeDB(0,1,10),
            SubscribeDB(0,1,12),
            SubscribeDB(0,1,13),
            SubscribeDB(0,1,16),
            SubscribeDB(0,1,17),
            SubscribeDB(0,1,19),
            SubscribeDB(0,1,21))
    }

//    fun getSampleVideoData(): VideoDB {
//        var seq = System.currentTimeMillis().toInt()
//
//        return VideoDB(seq,
//            1,
//            "비디오 제목 샘플",
//            "비디오 내용 샘플",
//            "http://data-dev.earlybird.ai:80/image/news/base/202008/18_173ff5638ea4fe1f.jpg",
//            "http://cache.midibus.kinxcdn.com/hls/ch_171e807a/173ff5638ea4fe1f/playlist.m3u8",
//            100,
//            5)
//    }
//
//    fun getRecommendVideoData() : List<VideoDB> {
//        var seq = System.currentTimeMillis().toInt()
//
//        val recommendVideo = ArrayList<VideoDB>()
//        for (j in 0 until 20) {
//            val sampleRecommendVideo = VideoDB(seq++,
//                1,
//                "비디오 제목 샘플",
//                "비디오 내용 샘플",
//                "http://data-dev.earlybird.ai:80/image/news/base/202008/18_173ff5638ea4fe1f.jpg",
//                "http://cache.midibus.kinxcdn.com/hls/ch_171e807a/173ff5638ea4fe1f/playlist.m3u8",
//                100,
//                5)
//            recommendVideo.add(sampleRecommendVideo)
//        }
//
//        return recommendVideo
//    }

    private fun getStatusBarHeight(context: Context): Int {
        var result = 0

        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if(resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }

        return result
    }

    private fun getNavigationBarWidth(context: Context): Int {
        val metrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(metrics)
        val usableWidth = metrics.widthPixels
        context.windowManager.defaultDisplay.getRealMetrics(metrics)
        val realWidth = metrics.widthPixels
        return if (realWidth > usableWidth) realWidth - usableWidth else 0
    }

    fun getSnsCount(count: Int): String {
        return "$count"
    }

    fun getSnsViewCount(count: Int): String {
        return when(count) {
            in 0..999 -> "${count}회"
            in 1000..9999 ->"${count/1000}천회"
            in 10000..9999999 -> "${count/10000}만회"
            else -> "${count/100000000}억회"
        }
    }

    fun getSnsUploadDate(date: Long): String {
        return "2개월 전"
    }
}