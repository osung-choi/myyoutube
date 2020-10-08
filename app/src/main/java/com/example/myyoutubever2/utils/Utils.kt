package com.example.myyoutubever2.utils

import android.content.Context
import android.util.TypedValue

object Utils {
    fun convertDpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    fun getDisplayWidth(context: Context) = context.resources.displayMetrics.widthPixels

    fun getDisplayHeight(context: Context) = context.resources.displayMetrics.heightPixels - getStatusBarHeight(context)

    fun getStatusBarHeight(context: Context): Int {
        var result = 0

        val resourceId = context.resources.getIdentifier("status_bar_height","dimen","android")
        if(resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }

        return result
    }
}