package com.example.myyoutubever2.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics
import android.util.TypedValue


object Utils {
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
        return if(getOrientation(context) == Configuration.ORIENTATION_PORTRAIT) {
            context.resources.displayMetrics.heightPixels - getStatusBarHeight(context)
        }else {
            context.resources.displayMetrics.heightPixels
        }

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