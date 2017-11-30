package io.dotlearn.lrnplayer.listener

import io.dotlearn.lrnplayer.LRNPlayerView

/**
 * Interface definition for a callback to be invoked when a vectorized video is being downloaded
 */
interface OnDownloadProgressListener {

    /**
     * Called when a chunk of the vectorized video file is downloaded.
     *
     * @param progressPercent The download progress percentage, from 0% to 100%.
     */
    fun onDownloadProgress(lrnPlayerView: LRNPlayerView, progressPercent: Float)

}