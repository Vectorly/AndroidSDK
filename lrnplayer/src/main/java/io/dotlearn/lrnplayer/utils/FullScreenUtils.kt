package io.dotlearn.lrnplayer.utils

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration

internal object FullScreenUtils {

    internal fun toggleOrientation(activity: Activity) {
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