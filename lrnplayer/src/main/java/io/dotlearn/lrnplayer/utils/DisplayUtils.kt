package io.dotlearn.lrnplayer.utils

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import android.util.DisplayMetrics

object DisplayUtils {

    fun px2dp(context: Context, px: Int): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay

        val displaymetrics = DisplayMetrics()
        display.getMetrics(displaymetrics)

        return (px / displaymetrics.density + 0.5f).toInt()
    }

    fun getScreenWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = Point()
        wm.defaultDisplay.getSize(size)
        return size.x
    }

    fun getScreenHeight(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = Point()
        wm.defaultDisplay.getSize(size)
        return size.y
    }

    fun calculateHeightBasedOnWidthAndAspectRatio(aspectRatio: Double, width: Int): Int {
        return Math.ceil((width / aspectRatio)).toInt()
    }

}
