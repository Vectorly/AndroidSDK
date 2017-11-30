package io.dotlearn.lrnplayer.listener

import io.dotlearn.lrnplayer.LRNPlayerView

/**
 * Interface definition for a callback to be invoked when a vectorized video is being downloaded
 */
interface OnDownloadProgressListener {

    /**
     * Called when a chunk of the vectorized video file is downloaded.
     *
     * @param progress The download progress, from 0 to 1. Where 0 represent zero percent and 1
     * represents 100%
     */
    fun onDownloadProgress(lrnPlayerView: LRNPlayerView, progress: Float)

}