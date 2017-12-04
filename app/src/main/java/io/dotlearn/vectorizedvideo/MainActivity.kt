package io.dotlearn.vectorizedvideo

import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import io.dotlearn.lrnplayer.LRNPlayerView
import io.dotlearn.lrnplayer.error.LRNPlayerException
import io.dotlearn.lrnplayer.listener.*
import io.dotlearn.lrnplayer.model.Metadata
import android.view.WindowManager
import android.view.Display
import android.R.attr.orientation
import android.content.Context
import android.content.res.Configuration


class MainActivity : AppCompatActivity() {

    private val LOG_TAG = "LMainActivity"
    private lateinit var lrnPlayerView: LRNPlayerView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjbGllbnRfaWQiOiJkb3RsZWFybl9pbyIsInR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJzYW5kYm94Ijp0cnVlLCJ0b2tlbl9pZCI6IjlhM2QwODMwLWE5NTMtNGE0Yi1iOWY5LTI3ZjZlODg4ODdmZCJ9.lMRw8ELT5unXc6BxYFQqv2g5Ysg9jWW48zy2WiFwcoo";
        val videoId = "6320ea83-a12e-4091-bce6-3060ceba29bc"


        lrnPlayerView = findViewById(R.id.lrn_player_view)
        lrnPlayerView.debug(true)

        lrnPlayerView.prepare(token, videoId, true, object: OnPreparedListener {

            override fun onPrepared(lrnPlayerView: LRNPlayerView) {
                lrnPlayerView.start()
            }

        })

        /*Handler().postDelayed({
            lrnPlayerView.prepare(token, "a3a39100-6c60-4db1-bc18-f70e77af272a",
                    true, object: OnPreparedListener {

                override fun onPrepared(lrnPlayerView: LRNPlayerView) {
                    lrnPlayerView.start()
                }

            })
        },
                20000)*/

        lrnPlayerView.setOnErrorListener(object: OnErrorListener {

            override fun onError(lrnPlayerView: LRNPlayerView, e: LRNPlayerException) {
                Log.d(LOG_TAG, "onError: $e")
            }

        })

        lrnPlayerView.setOnMetadataLoadedListener(object: OnMetadataLoadedListener {

            override fun onMetadataLoaded(lrnPlayerView: LRNPlayerView, metadata: Metadata) {
                Log.d(LOG_TAG, "onMetadataLoaded: $metadata")
            }

        })

        lrnPlayerView.setOnDownloadListener(object: OnDownloadProgressListener {

            override fun onDownloadProgress(lrnPlayerView: LRNPlayerView, progressPercent: Float) {
                Log.d(LOG_TAG, "onDownloadProgress: $progressPercent")
            }

        })

        lrnPlayerView.setOnCompletionListener(object: OnPlaybackCompletionListener {

            override fun onPlaybackCompletion(lrnPlayerView: LRNPlayerView) {
                Log.d(LOG_TAG, "onPlaybackCompletion")
            }

        })

        lrnPlayerView.setOnFullScreenToggledListener(object: OnFullScreenToggledListener {

            override fun onFullScreenToggled(lrnPlayerView: LRNPlayerView) {
                val display = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
                val orientation = display.orientation

                when (orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    Configuration.ORIENTATION_LANDSCAPE -> requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
            }

        })

    }

    @Override
    override fun onPause() {
        super.onPause()
        lrnPlayerView.pause()
    }

    @Override
    override fun onResume() {
        super.onResume()
        lrnPlayerView.start()
    }

    @Override
    override fun onDestroy() {
        super.onDestroy()
        lrnPlayerView.release()
    }

}
