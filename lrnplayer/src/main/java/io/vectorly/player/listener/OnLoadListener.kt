package io.vectorly.player.listener

import io.vectorly.player.VectorlyPlayer

/**
 * Interface definition for a callback to be invoked when the Vectorized video is ready for playback.
 */
interface OnLoadListener {

    /**
     * Called when the vectorized video is ready for playback.
     *
     * @param vectorlyPlayer: the VectorlyPlayer that is ready for playback
     */
    fun onLoaded(vectorlyPlayer: VectorlyPlayer)

}