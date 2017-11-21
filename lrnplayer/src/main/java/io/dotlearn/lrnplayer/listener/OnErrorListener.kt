package io.dotlearn.lrnplayer.listener

import io.dotlearn.lrnplayer.error.ErrorCode
import io.dotlearn.lrnplayer.LRNPlayerView

/**
 * Interface definition of a callback to be invoked when there has been an error during an operation.
 */
interface OnErrorListener {

    /**
     * Called to indicate an error.
     *
     * @param lrnPlayerView the LRNPlayerView the error pertains to
     * @param errorCode An [ErrorCode] specific to the error that occurred
     * @return True if the method handled the error, false if it didn't. Returning false,
     * or not having an OnErrorListener at all, will cause the OnCompletionListener to be called.
     */
    fun onError (lrnPlayerView: LRNPlayerView, errorCode: ErrorCode): Boolean

}