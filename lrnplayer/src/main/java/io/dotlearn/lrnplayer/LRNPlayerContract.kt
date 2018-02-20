package io.dotlearn.lrnplayer

import io.dotlearn.lrnplayer.error.LRNPlayerException
import io.dotlearn.lrnplayer.listener.*
import io.dotlearn.lrnplayer.loader.model.VideoMetadata

internal interface LRNPlayerContract {

    interface PlayerView {

        fun prepare(accessToken: String, videoId: String, onPrepareListener: OnPreparedListener)
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

        fun showError(errorMsg: String)
        fun showDownloadProgress(downloadPercentage: Int)

    }

    interface PlayerWebInterface {

        fun onMediaPrepared()
        fun onMetadata(videoMetadata: VideoMetadata)
        fun onError(errorMsg: String)
        fun onError(e: LRNPlayerException)
        fun onDownloadProgress(bytesTransferred: Long, totalBytes: Long)
        fun onGetPosition(position: Long)
        fun onPlaybackCompleted()
        fun onFullScreenToggled()
        fun log(message: String)

    }

}