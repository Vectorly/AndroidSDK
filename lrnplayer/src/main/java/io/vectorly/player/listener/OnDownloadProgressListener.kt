package io.vectorly.player.listener

import io.vectorly.player.VectorlyPlayer

/**
 * Interface definition for a callback to be invoked when a vectorized video is being downloaded
 */
interface OnDownloadProgressListener {

    /**
     * Called when a chunk of the vectorized video file is downloaded.
     *
     * @param vectorlyPlayer The VectorlyPlayer that is downloading the video
     * @param progressPercent The download progress percentage, from 0 to 100.
     */
    fun onDownloadProgress(vectorlyPlayer: VectorlyPlayer, progressPercent: Int)

}