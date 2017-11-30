package io.dotlearn.lrnplayer

import io.dotlearn.lrnplayer.listener.OnPlaybackCompletionListener
import io.dotlearn.lrnplayer.listener.OnDownloadProgressListener
import io.dotlearn.lrnplayer.listener.OnErrorListener
import io.dotlearn.lrnplayer.listener.OnPreparedListener

internal interface LRNPlayerContract {

    interface PlayerView {

        fun prepare(accessToken: String, videoId: String, autoStart: Boolean,
                    onPrepareListener: OnPreparedListener)
        fun setOnDownloadListener(downloadProgressListener: OnDownloadProgressListener)
        fun setOnCompletionListener(completionListener: OnPlaybackCompletionListener)
        fun setOnErrorListener(errorListener: OnErrorListener)

        fun start()
        fun pause()
        fun seekTo(seekPos: Long)

        fun release()
    }

    interface PlayerWebInterface {

        fun onMediaPrepared()
        fun onMetadata(metaData: String)
        fun onError(e: String)
        fun onError(errorCode: ErrorCode)
        fun onDownloadProgress(progress: Float)
        fun onGetPosition(position: Long)
        fun onPlaybackCompleted()
        fun log(format: String)

    }

}