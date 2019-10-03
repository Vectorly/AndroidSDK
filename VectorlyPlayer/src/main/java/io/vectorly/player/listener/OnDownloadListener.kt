package io.vectorly.player.listener


/**
 * Interface definition for a callback to be invoked when a vectorized video is being downloaded
 */
interface DownloadListener {

    /**
     * Called when a chunk of the vectorized video file is downloaded.
     *
     */
     fun onDownloadStarted(downloadTag: String)
    fun onDownloadProgressUpdate(downloadTag: String, bytesTransferred: Long, totalBytes: Long)
     fun onDownloadError(downloadTag: String, e: Exception)
    fun onDownloadCompleted(downloadTag: String)


}