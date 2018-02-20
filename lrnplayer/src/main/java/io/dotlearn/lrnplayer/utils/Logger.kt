package io.dotlearn.lrnplayer.utils

import android.util.Log

object Logger {

    private const val LOG_TAG = "LrnPlayer"
    internal const val LOG_ENABLED = true

    fun e(message: String) {
        if(LOG_ENABLED) {
            Log.e(LOG_TAG, message)
        }
    }

    fun d(message: String) {
        if(LOG_ENABLED) {
            Log.d(LOG_TAG, message)
        }
    }

}