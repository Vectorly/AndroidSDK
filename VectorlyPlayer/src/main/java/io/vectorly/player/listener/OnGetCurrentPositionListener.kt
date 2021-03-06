package io.vectorly.player.listener

import io.vectorly.player.VectorlyPlayer

/**
 * Interface definition for a callback to be invoked when the current playback position of a video
 * is requested.
 */
interface OnGetCurrentPositionListener {

    /**
     * Called when the current playback position of a video has been been known after requesting for it
     *
     * @param vectorlyPlayer The VectorlyPlayer
     * @param currentPlaybackPosition The current video playback position
     */
    fun onCurrentPlaybackPositionGotten(vectorlyPlayer: VectorlyPlayer, currentPlaybackPosition: Long)

}