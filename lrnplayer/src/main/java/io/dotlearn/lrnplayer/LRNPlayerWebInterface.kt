package io.dotlearn.lrnplayer

import android.webkit.JavascriptInterface
import io.dotlearn.lrnplayer.error.LRNPlayerException
import io.dotlearn.lrnplayer.listener.*
import io.dotlearn.lrnplayer.loader.model.VideoMetadata
import io.dotlearn.lrnplayer.utils.Logger
import kotlin.math.roundToInt

internal class LRNPlayerWebInterface(private val lrnPlayerView: LRNPlayerView){

    internal var prepareListener: OnPreparedListener? = null
    internal var completionListener: OnPlaybackCompletionListener? = null
    internal var downloadProgressListener: OnDownloadProgressListener? = null
    internal var errorListener: OnErrorListener? = null
    internal var metadataLoadedListener: OnMetadataLoadedListener? = null
    internal var fullScreenToggledListener: OnFullScreenToggledListener? = null
    internal var getCurrentPositionListener: OnGetCurrentPositionListener? = null

    @JavascriptInterface
    fun onGetPosition(position: Long) {
        log("onGetPosition($position)")
        getCurrentPositionListener?.onCurrentPlaybackPositionGotten(lrnPlayerView, position)
    }

    @JavascriptInterface
    fun onError(errorMsg: String) {
        log("onError($errorMsg)")
        onError(LRNPlayerException(errorMsg))
    }

    internal fun onError(e: LRNPlayerException) {
        log("onError($e")
        lrnPlayerView.showError("An error occurred while loading video")

        if(errorListener == null) {
            throw e
        }
        else {
            lrnPlayerView.post({ errorListener?.onError(lrnPlayerView, e) })
        }
    }

    internal fun onMetadata(videoMetadata: VideoMetadata) {
        lrnPlayerView.post({
            metadataLoadedListener?.onMetadataLoaded(lrnPlayerView, videoMetadata)
        })
    }

    internal fun onDownloadProgress(bytesTransferred: Long, totalBytes: Long) {
        lrnPlayerView.post({
            Logger.d("onDownloadProgress. Transferred: $bytesTransferred. Total: $totalBytes")
            val downloadPercentage = (bytesTransferred.toDouble() / totalBytes.toDouble()) * 100.0
            val downloadPercentageRounded = downloadPercentage.roundToInt()
            downloadProgressListener?.onDownloadProgress(lrnPlayerView, downloadPercentageRounded)
            lrnPlayerView.showDownloadProgress(downloadPercentageRounded)
        })
    }

    @JavascriptInterface
    fun onMediaPrepared() {
        log("onMediaPrepared()")
        lrnPlayerView.post({
            lrnPlayerView.onPrepared()
            prepareListener?.onPrepared(lrnPlayerView)
        })
    }

    @JavascriptInterface
    fun onPlaybackCompleted() {
        log("onPlaybackCompleted()")
        lrnPlayerView.post({ completionListener?.onPlaybackCompletion(lrnPlayerView) })
    }

    @JavascriptInterface
    fun onFullScreenToggled() {
        log("onFullScreenToggled")
        lrnPlayerView.post({ fullScreenToggledListener?.onFullScreenToggled(lrnPlayerView) })
    }

    @JavascriptInterface
    fun log(message: String) {
        Logger.d(message)
    }

}