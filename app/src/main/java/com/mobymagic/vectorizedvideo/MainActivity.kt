package com.mobymagic.vectorizedvideo

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import io.dotlearn.lrnplayer.LRNPlayerView
import io.dotlearn.lrnplayer.error.LRNPlayerException
import io.dotlearn.lrnplayer.listener.*
import io.dotlearn.lrnplayer.loader.model.VideoMetadata
import io.dotlearn.lrnplayer.utils.FullScreenUtils
import io.dotlearn.vectorizedvideo.R
import kotlinx.android.synthetic.main.activity_main.*

const val EXTRA_VIDEO_ID = "EXTRA_VIDEO_ID"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupAllListeners()
        setupPlaybackControls()

        prepareVideo("48fe698e-22b0-4528-9722-26ed00ab79ef")
    }


    private fun setupPlaybackControls() {

        setupSeekBarControls()
    }

    private fun setupSeekBarControls() {

    }

    private fun prepareVideo(videoId: String) {
        val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ2aWRlb19pZCI6IjQ4ZmU2OThlLTIyYjAtNDUyOC05NzIyLTI2ZWQwMGFiNzllZiIsImV4cGlyeSI6MTU3MjAzODExMzIxMn0.xG0vh_Gko2Vc8Mk8K57jaIx4FuDrFYYX6oWBxH62CTc"
        lrnPlayerView.prepare(videoId,  token,true, object: OnPreparedListener {

            override fun onPrepared(lrnPlayerView: LRNPlayerView) {

            }

        })
    }

    private fun setupAllListeners() {
        lrnPlayerView.setOnErrorListener(object: OnErrorListener {

            override fun onError(lrnPlayerView: LRNPlayerView, e: LRNPlayerException) {

            }

        })

        lrnPlayerView.setOnMetadataLoadedListener(object: OnMetadataLoadedListener {

            override fun onMetadataLoaded(lrnPlayerView: LRNPlayerView, metadata: VideoMetadata) {

            }

        })

        lrnPlayerView.setOnDownloadListener(object: OnDownloadProgressListener {

            override fun onDownloadProgress(lrnPlayerView: LRNPlayerView, progressPercent: Int) {

            }

        })

        lrnPlayerView.setOnCompletionListener(object: OnPlaybackCompletionListener {

            override fun onPlaybackCompletion(lrnPlayerView: LRNPlayerView) {

            }

        })

        lrnPlayerView.setOnFullScreenToggledListener(object: OnFullScreenToggledListener {

            override fun onFullScreenToggled(lrnPlayerView: LRNPlayerView) {
                FullScreenUtils.toggleOrientation(this@MainActivity)
            }

        })
    }

    @Override
    override fun onPause() {
        super.onPause()
        // When the activity is paused, also pause the video playback
        lrnPlayerView.pause()
    }

    @Override
    override fun onDestroy() {
        super.onDestroy()
        // When the activity is being destroyed or the player is no longer needed, call the release
        // function to free up system resources
        lrnPlayerView.release()
    }

}
