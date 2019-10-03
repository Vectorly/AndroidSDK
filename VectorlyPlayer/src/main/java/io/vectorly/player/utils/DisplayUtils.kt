package io.vectorly.player.utils

import android.graphics.Point
import android.util.DisplayMetrics
import android.view.WindowManager

internal class DisplayUtils(private val windowManager: WindowManager) {

    internal fun px2dp(px: Int): Int {
        val display = windowManager.defaultDisplay

        val displayMetrics = DisplayMetrics()
        display.getMetrics(displayMetrics)

        return (px / displayMetrics.density + 0.5f).toInt()
    }

    private fun getScreenHeight(): Int {
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        return size.y
    }

    internal fun getUsableScreenHeight(): Int {
        var screenHeight = px2dp(getScreenHeight())
        screenHeight -= getControlsHeight(screenHeight)
        return screenHeight
    }

    private fun getControlsHeight(screenHeight: Int) = (screenHeight * 0.18).toInt()

    internal fun calculateHeightBasedOnWidthAndAspectRatio(aspectRatio: Double, width: Int): Int {
        return Math.ceil((width / aspectRatio)).toInt()
    }

}
