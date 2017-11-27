package io.dotlearn.lrnplayer.listener

import io.dotlearn.lrnplayer.LRNPlayerView

/**
 * Interface definition for a callback to be invoked when a vectorized video is being downloaded
 */
interface OnDownloadProgressListener {

    /**
     * Called when a chunk of the vectorized video file is downloaded. To calculate the percentage
     * of the vectorized video that has been downloaded, divide [downloadedBytes] by [totalBytes].
     * Example: ([downloadedBytes] / [totalBytes]) * 100f
     *
     * @param downloadedBytes The number of bytes downloaded
     * @param totalBytes The total size of the vectorized video file in bytes
     */
    fun onDownloadProgress(lrnPlayerView: LRNPlayerView, downloadedBytes: Long, totalBytes: Long)

}