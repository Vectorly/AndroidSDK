package io.dotlearn.lrnplayer

import io.dotlearn.lrnplayer.error.LRNPlayerException
import io.dotlearn.lrnplayer.listener.*

internal interface LRNPlayerContract {

    interface PlayerView {

        fun prepare(accessToken: String, videoId: String, autoStart: Boolean,
                    onPrepareListener: OnPreparedListener)
        fun setOnDownloadListener(downloadProgressListener: OnDownloadProgressListener)
        fun setOnCompletionListener(completionListener: OnPlaybackCompletionListener)
        fun setOnErrorListener(errorListener: OnErrorListener)
        fun setOnMetadataLoadedListener(metadataLoadedListener: OnMetadataLoadedListener)
        fun setOnFullScreenToggledListener(fullScreenToggledListener: OnFullScreenToggledListener)
        fun setOnGetCurrentPositionListener(getCurrentPositionListener: OnGetCurrentPositionListener)

        fun start()
        fun pause()
        fun seekTo(seekPos: Long)

        fun release()
    }

    interface PlayerWebInterface {

        fun onMediaPrepared()
        fun onMetadata(metaData: String)
        fun onError(errorMsg: String)
        fun onError(e: LRNPlayerException)
        fun onDownloadProgress(progress: Float)
        fun onGetPosition(position: Long)
        fun onPlaybackCompleted()
        fun onFullScreenToggled()
        fun log(format: String)

    }

}