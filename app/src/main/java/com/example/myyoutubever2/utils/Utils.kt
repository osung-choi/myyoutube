package com.example.myyoutubever2.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics
import android.util.TypedValue
import com.example.myyoutubever2.data.Video


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

    fun getSampleVideoData(): Video {
        var seq = 1

        val recommendVideoList = arrayListOf<Video>()
        for (j in 0 until 10) {
            val sampleRecommendVideo = Video(seq++,
                "비디오 제목 샘플",
                "비디오 내용 샘플",
                "http://data-dev.earlybird.ai:80/image/news/base/202008/18_173ff5638ea4fe1f.jpg",
                "http://cache.midibus.kinxcdn.com/hls/ch_171e807a/173ff5638ea4fe1f/playlist.m3u8",
                100,
                5)
            recommendVideoList.add(sampleRecommendVideo)
        }

        return Video(seq,
            "비디오 제목 샘플",
            "비디오 내용 샘플",
            "http://data-dev.earlybird.ai:80/image/news/base/202008/18_173ff5638ea4fe1f.jpg",
            "http://cache.midibus.kinxcdn.com/hls/ch_171e807a/173ff5638ea4fe1f/playlist.m3u8",
            100,
            5,
            recommendVideoList)
    }

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
}