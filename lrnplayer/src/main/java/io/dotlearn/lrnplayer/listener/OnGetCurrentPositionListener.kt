package io.dotlearn.lrnplayer.listener

import io.dotlearn.lrnplayer.LRNPlayerView

/**
 * Interface definition for a callback to be invoked when the current playback position of a video
 * is requested.
 */
interface OnGetCurrentPositionListener {

    /**
     * Called when the current playback position of a video has been been known after requesting for it
     *
     * @param lrnPlayerView The LRNPlayerView
     * @param currentPlaybackPosition The current video playback position
     */
    fun onCurrentPlaybackPositionGotten(lrnPlayerView: LRNPlayerView, currentPlaybackPosition: Long)

}