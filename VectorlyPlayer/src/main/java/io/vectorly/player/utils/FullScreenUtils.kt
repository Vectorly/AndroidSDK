package io.vectorly.player.utils

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration

object FullScreenUtils {

    fun toggleOrientation(activity: Activity) {
        val orientation = activity.resources.configuration.orientation

        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
            Configuration.ORIENTATION_LANDSCAPE -> {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        }
    }

}