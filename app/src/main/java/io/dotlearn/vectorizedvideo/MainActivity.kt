package io.dotlearn.vectorizedvideo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.dotlearn.lrnplayer.LRNPlayerView
import io.dotlearn.lrnplayer.error.ErrorCode
import io.dotlearn.lrnplayer.listener.OnCompletionListener
import io.dotlearn.lrnplayer.listener.OnDownloadProgressListener
import io.dotlearn.lrnplayer.listener.OnErrorListener
import io.dotlearn.lrnplayer.listener.OnPreparedListener

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val accessToken = "accessToken"
        val videoId = "videoId"

        val lrnPlayerView = findViewById<LRNPlayerView>(R.id.lrn_player_view)
        lrnPlayerView.prepare(accessToken, videoId, object: OnPreparedListener {

            override fun onPrepared(lrnPlayerView: LRNPlayerView) {
                lrnPlayerView.start()
            }

        }
        )

        lrnPlayerView.setOnCompletionListener(object: OnCompletionListener {

            override fun onCompletion(lrnPlayerView: LRNPlayerView) {
            }

        })
        lrnPlayerView.setOnErrorListener(object: OnErrorListener {

            override fun onError(lrnPlayerView: LRNPlayerView, errorCode: ErrorCode): Boolean {
                return false
            }

        })
        lrnPlayerView.setOnDownloadListener(object: OnDownloadProgressListener{

            override fun onDownloadProgress(downloadedBytes: Long, totalBytes: Long) {
            }

        })
    }

}
