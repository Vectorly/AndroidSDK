package io.dotlearn.lrnplayer.listener

import io.dotlearn.lrnplayer.error.LRNPlayerException
import io.dotlearn.lrnplayer.LRNPlayerView

/**
 * Interface definition of a callback to be invoked when there has been an error during an operation.
 * Not having an OnErrorListener at all, will cause an exception to be thrown.
 */
interface OnErrorListener {

    /**
     * Called to indicate an error just occurred.
     *
     * @param lrnPlayerView the LRNPlayerView the error pertains to
     * @param e The exception that occurred
     */
    fun onError(lrnPlayerView: LRNPlayerView, e: LRNPlayerException)

}