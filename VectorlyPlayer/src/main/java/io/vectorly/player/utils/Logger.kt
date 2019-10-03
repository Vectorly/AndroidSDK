package io.vectorly.player.utils

import android.util.Log
import io.vectorly.player.BuildConfig

internal object Logger {

    private const val LOG_TAG = "LrnPlayer"

    internal fun e(message: String) {
        if(BuildConfig.DEBUG) {
            Log.e(LOG_TAG, message)
        }
    }

    internal fun d(message: String) {
        if(BuildConfig.DEBUG) {
            Log.d(LOG_TAG, message)
        }
    }

}