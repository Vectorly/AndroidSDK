package io.dotlearn.lrnplayer.listener

import io.dotlearn.lrnplayer.error.ErrorCode
import io.dotlearn.lrnplayer.LRNPlayerView

/**
 * Interface definition of a callback to be invoked when there has been an error during an operation.
 */
interface OnErrorListener {

    /**
     * Called to indicate an error. Not having an OnErrorListener at all, will cause an exception to be thrown.
     *
     * @param lrnPlayerView the LRNPlayerView the error pertains to
     * @param errorCode An [ErrorCode] specific to the error that occurred
     */
    fun onError (lrnPlayerView: LRNPlayerView, errorCode: ErrorCode)

}