package io.vectorly.player

import android.webkit.JavascriptInterface
import io.vectorly.player.error.LRNPlayerException
import io.vectorly.player.listener.*
import io.vectorly.player.loader.model.VideoMetadata
import io.vectorly.player.utils.Logger
import kotlin.math.roundToInt

internal class VectorlyPlayerWebInterface(private val vectorlyPlayer: VectorlyPlayer){

    internal var prepareListener: OnLoadListener? = null
    internal var completionListener: OnPlaybackCompletionListener? = null
    internal var downloadProgressListener: OnDownloadProgressListener? = null
    internal var errorListener: OnErrorListener? = null
    internal var metadataLoadedListener: OnMetadataLoadedListener? = null
    internal var fullScreenToggledListener: OnFullScreenToggledListener? = null
    internal var getCurrentPositionListener: OnGetCurrentPositionListener? = null

    @JavascriptInterface
    fun onGetPosition(position: Long) {
        log("onGetPosition($position)")
        getCurrentPositionListener?.onCurrentPlaybackPositionGotten(vectorlyPlayer, position)
    }

    @JavascriptInterface
    fun onError(errorMsg: String) {
        log("onError($errorMsg)")
        onError(LRNPlayerException(errorMsg))
    }



    internal fun onError(e: LRNPlayerException) {
        log("onError($e")
        vectorlyPlayer.showError("An error occurred while loading video")

        if(errorListener == null) {
            throw e
        }
        else {
            vectorlyPlayer.post({ errorListener?.onError(vectorlyPlayer, e) })
        }
    }

    internal fun onMetadata(videoMetadata: VideoMetadata) {
        vectorlyPlayer.post({
            metadataLoadedListener?.onMetadataLoaded(vectorlyPlayer, videoMetadata)
        })
    }

    internal fun onDownloadProgress(bytesTransferred: Long, totalBytes: Long) {
        vectorlyPlayer.post({
            Logger.d("onDownloadProgress. Transferred: $bytesTransferred. Total: $totalBytes")
            val downloadPercentage = (bytesTransferred.toDouble() / totalBytes.toDouble()) * 100.0
            val downloadPercentageRounded = downloadPercentage.roundToInt()
            downloadProgressListener?.onDownloadProgress(vectorlyPlayer, downloadPercentageRounded)
        })
    }

    @JavascriptInterface
    fun onMediaPrepared() {
        log("onMediaPrepared()")
        vectorlyPlayer.post({
            vectorlyPlayer.onPrepared()
            prepareListener?.onLoaded(vectorlyPlayer)
        })
    }

    @JavascriptInterface
    fun onPlaybackCompleted() {
        log("onPlaybackCompleted()")
        vectorlyPlayer.post({ completionListener?.onPlaybackCompletion(vectorlyPlayer) })
    }

    @JavascriptInterface
    fun onFullScreenToggled() {
        log("onFullScreenToggled")
        vectorlyPlayer.post({ fullScreenToggledListener?.onFullScreenToggled(vectorlyPlayer) })
    }

    @JavascriptInterface
    fun log(message: String) {
        Logger.d(message)
    }

}