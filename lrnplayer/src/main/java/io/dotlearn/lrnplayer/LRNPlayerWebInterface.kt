package io.dotlearn.lrnplayer

import android.util.Log
import android.webkit.JavascriptInterface
import io.dotlearn.lrnplayer.error.ErrorCode
import io.dotlearn.lrnplayer.error.LRNPlayerException
import io.dotlearn.lrnplayer.listener.*

internal class LRNPlayerWebInterface(private val lrnPlayerView: LRNPlayerView):
        LRNPlayerContract.PlayerWebInterface {

    internal var debug = false

    // region Listener Variables
    internal var prepareListener: OnPreparedListener? = null
    internal var completionListener: OnPlaybackCompletionListener? = null
    internal var downloadProgressListener: OnDownloadProgressListener? = null
    internal var errorListener: OnErrorListener? = null
    internal var metadataLoadedListener: OnMetadataLoadedListener? = null
    // endregion

    @JavascriptInterface
    override fun onMediaPrepared() {
        log(debug, "OnMediaPrepared called")
        prepareListener?.onPrepared(lrnPlayerView)
    }

    @JavascriptInterface
    override fun onMetadata(metaData: String) {
        log(debug, "OnMetadata called")

        metadataLoadedListener?.onMetadataLoaded(lrnPlayerView, metaData)
    }

    @JavascriptInterface
    override fun onError(e: String) {
        log(debug, "onError called")

        // todo check if error listener is attached
        errorListener?.onError(lrnPlayerView, ErrorCode.valueOf(e))
    }

    override fun onError(errorCode: ErrorCode) {
        log(debug, "onError called")
        log(debug, "Error code: ${errorCode.code}, Error message: ${errorCode.message}")

        if(errorListener == null) {
            throw LRNPlayerException(errorCode)
        }
        else {
            errorListener?.onError(lrnPlayerView, errorCode)
        }
    }

    @JavascriptInterface
    override fun onDownloadProgress(bytesDownloaded: Long, totalBytes: Long) {
        log(debug, "OnDownloadProgress called")
        log(debug, "Bytes downloaded = $bytesDownloaded, Total bytes = $totalBytes")

        downloadProgressListener?.onDownloadProgress(lrnPlayerView, bytesDownloaded, totalBytes)
    }

    @JavascriptInterface
    override fun onPlaybackCompleted() {
        log(debug, "OnPlaybackCompleted called")

        completionListener?.onPlaybackCompletion(lrnPlayerView)
    }

    @JavascriptInterface
    override fun log(format: String) {
        log(debug, format)
    }

    private companion object {

        private val LOG_TAG = "LRNPlayerWebInterface"

        fun log(debug: Boolean, message: String) {
            if(debug) {
                Log.d(LOG_TAG, message)
            }
        }

    }

}