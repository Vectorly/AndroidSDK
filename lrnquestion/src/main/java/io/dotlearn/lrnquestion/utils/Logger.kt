package io.dotlearn.lrnquestion.utils

import android.util.Log
import io.dotlearn.lrnquestion.BuildConfig

internal object Logger {

    private const val LOG_TAG = "LrnQuestion"

    fun e(message: String) {
        if(BuildConfig.DEBUG) {
            Log.e(LOG_TAG, message)
        }
    }

    fun d(message: String) {
        if(BuildConfig.DEBUG) {
            Log.d(LOG_TAG, message)
        }
    }

}