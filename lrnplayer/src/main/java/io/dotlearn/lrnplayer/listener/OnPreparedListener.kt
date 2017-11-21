package io.dotlearn.lrnplayer.listener

import io.dotlearn.lrnplayer.LRNPlayerView

/**
 * Interface definition for a callback to be invoked when the Vectorized video is ready for playback.
 */
interface OnPreparedListener {

    /**
     * Called when the vectorized video is ready for playback.
     *
     * @param lrnPlayerView: the LRNPlayerView that is ready for playback
     */
    fun onPrepared(lrnPlayerView: LRNPlayerView)

}