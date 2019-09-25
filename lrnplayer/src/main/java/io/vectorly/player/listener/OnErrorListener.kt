package io.vectorly.player.listener

import io.vectorly.player.error.LRNPlayerException
import io.vectorly.player.VectorlyPlayer

/**
 * Interface definition of a callback to be invoked when there has been an error during an operation.
 * Not having an OnErrorListener at all, will cause an exception to be thrown.
 */
interface OnErrorListener {

    /**
     * Called to indicate an error just occurred.
     *
     * @param vectorlyPlayer the VectorlyPlayer the error pertains to
     * @param e The exception that occurred
     */
    fun onError(vectorlyPlayer: VectorlyPlayer, e: LRNPlayerException)

}