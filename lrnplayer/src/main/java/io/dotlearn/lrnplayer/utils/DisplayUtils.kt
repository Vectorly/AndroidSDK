package io.dotlearn.lrnplayer.utils

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

object DisplayUtils {

    fun getScreenWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = Point()
        wm.defaultDisplay.getSize(size)
        return size.x
    }

    fun getHeightToMaintain169AspectRatio(width: Int): Int {
        val aspectRatio = 1.77777778
        return (width / aspectRatio).toInt()
    }

}
