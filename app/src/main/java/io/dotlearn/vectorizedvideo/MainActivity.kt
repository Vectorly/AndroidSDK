package io.dotlearn.vectorizedvideo

import android.content.Context
import android.graphics.Point
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import io.dotlearn.lrnplayer.LRNPlayerView
import io.dotlearn.lrnplayer.error.ErrorCode
import io.dotlearn.lrnplayer.listener.OnPlaybackCompletionListener
import io.dotlearn.lrnplayer.listener.OnDownloadProgressListener
import io.dotlearn.lrnplayer.listener.OnErrorListener
import io.dotlearn.lrnplayer.listener.OnPreparedListener

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.e("TAGGA", "Width: " + getScreenWidth())
        val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjbGllbnRfaWQiOiJkb3RsZWFybl9pbyIsInR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJzYW5kYm94Ijp0cnVlLCJ0b2tlbl9pZCI6IjlhM2QwODMwLWE5NTMtNGE0Yi1iOWY5LTI3ZjZlODg4ODdmZCJ9.lMRw8ELT5unXc6BxYFQqv2g5Ysg9jWW48zy2WiFwcoo";
        val videoId = "b78154c8-4d28-4cdc-b672-a2d6ef9f3810"


        val lrnPlayerView = findViewById<LRNPlayerView>(R.id.lrn_player_view)
        lrnPlayerView.debug(true)

        Handler().postDelayed({
            lrnPlayerView.prepare(token, videoId, true, object: OnPreparedListener {

                override fun onPrepared(lrnPlayerView: LRNPlayerView) {
                    lrnPlayerView.start()
                }

            }
            )
        }, 2000)

        /*lrnPlayerView.setOnCompletionListener(object: OnPlaybackCompletionListener {

            override fun onPlaybackCompletion(lrnPlayerView: LRNPlayerView) {
            }

        })

        lrnPlayerView.setOnErrorListener(object: OnErrorListener {

            override fun onError(lrnPlayerView: LRNPlayerView, errorCode: ErrorCode) {
            }

        })

        lrnPlayerView.setOnDownloadListener(object: OnDownloadProgressListener{

            override fun onDownloadProgress(lrnPlayerView: LRNPlayerView, downloadedBytes: Long, totalBytes: Long) {
            }

        })*/
    }

    fun getScreenWidth(): Int {
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        return size.x
    }

}
