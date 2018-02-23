package io.dotlearn.lrnplayer

import android.webkit.JavascriptInterface
import io.dotlearn.lrnplayer.error.LRNPlayerException
import io.dotlearn.lrnplayer.listener.*
import io.dotlearn.lrnplayer.loader.model.VideoMetadata
import io.dotlearn.lrnplayer.utils.Logger

internal class LRNPlayerWebInterface(private val lrnPlayerView: LRNPlayerView){

    internal var prepareListener: OnPreparedListener? = null
    internal var completionListener: OnPlaybackCompletionListener? = null
    internal var downloadProgressListener: OnDownloadProgressListener? = null
    internal var errorListener: OnErrorListener? = null
    internal var metadataLoadedListener: OnMetadataLoadedListener? = null
    internal var fullScreenToggledListener: OnFullScreenToggledListener? = null
    internal var getCurrentPositionListener: OnGetCurrentPositionListener? = null

    @JavascriptInterface
    internal fun onGetPosition(position: Long) {
        log("onGetPosition($position)")
        getCurrentPositionListener?.onCurrentPlaybackPositionGotten(lrnPlayerView, position)
    }

    @JavascriptInterface
    internal fun onError(errorMsg: String) {
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
            val downloadPercentage = ((bytesTransferred.toFloat() / totalBytes.toFloat()) * 100).toInt()
            downloadProgressListener?.onDownloadProgress(lrnPlayerView, downloadPercentage)
            lrnPlayerView.showDownloadProgress(downloadPercentage)
        })
    }

    @JavascriptInterface
    internal fun onMediaPrepared() {
        log("onMediaPrepared()")
        lrnPlayerView.post({
            lrnPlayerView.onPrepared()
            prepareListener?.onPrepared(lrnPlayerView)
        })
    }

    @JavascriptInterface
    internal fun onPlaybackCompleted() {
        log("onPlaybackCompleted()")
        lrnPlayerView.post({ completionListener?.onPlaybackCompletion(lrnPlayerView) })
    }

    @JavascriptInterface
    internal fun onFullScreenToggled() {
        log("onFullScreenToggled")
        lrnPlayerView.post({ fullScreenToggledListener?.onFullScreenToggled(lrnPlayerView) })
    }

    @JavascriptInterface
    internal fun log(message: String) {
        Logger.d(message)
    }

}