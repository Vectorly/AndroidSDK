package io.dotlearn.lrnplayer.listener

import io.dotlearn.lrnplayer.LRNPlayerView

/**
 * Interface definition for a callback to be invoked when playback of a vectorized video has completed.
 */
interface OnCompletionListener {

    /**
     * Called when the end of a vectorized video is reached during playback.
     *
     * @param lrnPlayerView: the LRNPlayerView that reached the end of the vectorized video file
     */
    fun onCompletion(lrnPlayerView: LRNPlayerView)

}