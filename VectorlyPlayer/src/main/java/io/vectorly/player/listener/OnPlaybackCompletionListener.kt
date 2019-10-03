package io.vectorly.player.listener

import io.vectorly.player.VectorlyPlayer

/**
 * Interface definition for a callback to be invoked when playback of a vectorized video has completed.
 */
interface OnPlaybackCompletionListener {

    /**
     * Called when the end of a vectorized video is reached during playback.
     *
     * @param vectorlyPlayer: the VectorlyPlayer that reached the end of the vectorized video file
     */
    fun onPlaybackCompletion(vectorlyPlayer: VectorlyPlayer)

}