package io.dotlearn.lrnplayer

import android.util.Log
import android.webkit.JavascriptInterface
import com.google.gson.Gson
import io.dotlearn.lrnplayer.error.LRNPlayerException
import io.dotlearn.lrnplayer.model.Metadata
import io.dotlearn.lrnplayer.listener.*

internal class LRNPlayerWebInterface(private val lrnPlayerView: LRNPlayerView):
        LRNPlayerContract.PlayerWebInterface {

    private val gson = Gson()
    internal var debug = false

    // region Listener Variables
    internal var prepareListener: OnPreparedListener? = null
    internal var completionListener: OnPlaybackCompletionListener? = null
    internal var downloadProgressListener: OnDownloadProgressListener? = null
    internal var errorListener: OnErrorListener? = null
    internal var metadataLoadedListener: OnMetadataLoadedListener? = null
    internal var fullScreenToggledListener: OnFullScreenToggledListener? = null
    internal var getCurrentPositionListener: OnGetCurrentPositionListener? = null
    // endregion

    @JavascriptInterface
    override fun onGetPosition(position: Long) {
        log(debug, "onGetPosition($position)")
        getCurrentPositionListener?.onCurrentPlaybackPositionGotten(lrnPlayerView, position)
    }

    @JavascriptInterface
    override fun onError(errorMsg: String) {
        log(debug, "onError($errorMsg)")
        onError(LRNPlayerException(errorMsg))
    }

    override fun onError(e: LRNPlayerException) {
        log(debug, "onError($e")

        if(errorListener == null) {
            throw e
        }
        else {
            lrnPlayerView.post({ errorListener?.onError(lrnPlayerView, e) })
        }
    }

    @JavascriptInterface
    override fun onMediaPrepared() {
        log(debug, "onMediaPrepared()")
        lrnPlayerView.post({
            lrnPlayerView.onPrepared()
            prepareListener?.onPrepared(lrnPlayerView)
        })
    }

    @JavascriptInterface
    override fun onMetadata(metaData: String) {
        log(debug, "onMetadata($metaData)")
        lrnPlayerView.post({ metadataLoadedListener?.onMetadataLoaded(lrnPlayerView,
                gson.fromJson(metaData, Metadata::class.java)) })
    }

    @JavascriptInterface
    override fun onDownloadProgress(progress: Float) {
        log(debug, "onDownloadProgress($progress)")
        lrnPlayerView.post({ lrnPlayerView.post({ downloadProgressListener?.
                onDownloadProgress(lrnPlayerView, progress) }) })
    }

    @JavascriptInterface
    override fun onPlaybackCompleted() {
        log(debug, "onPlaybackCompleted()")
        lrnPlayerView.post({ completionListener?.onPlaybackCompletion(lrnPlayerView) })
    }

    @JavascriptInterface
    override fun onFullScreenToggled() {
        log(debug, "onFullScreenToggled")
        lrnPlayerView.post({ fullScreenToggledListener?.onFullScreenToggled(lrnPlayerView) })
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