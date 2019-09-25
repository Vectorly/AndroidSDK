package com.mobymagic.vectorizedvideo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.vectorly.player.VectorlyPlayer
import io.vectorly.player.error.LRNPlayerException
import io.vectorly.player.listener.*
import io.vectorly.player.loader.model.VideoMetadata
import io.vectorly.player.utils.FullScreenUtils
import io.vectorly.vectorizedvideo.R
import kotlinx.android.synthetic.main.activity_main.*

const val EXTRA_VIDEO_ID = "EXTRA_VIDEO_ID"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupAllListeners()
        setupPlaybackControls()

        loadVideo("48fe698e-22b0-4528-9722-26ed00ab79ef")
    }


    private fun setupPlaybackControls() {

        setupSeekBarControls()
    }

    private fun setupSeekBarControls() {

    }

    private fun loadVideo(videoId: String) {
        val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ2aWRlb19pZCI6IjQ4ZmU2OThlLTIyYjAtNDUyOC05NzIyLTI2ZWQwMGFiNzllZiIsImV4cGlyeSI6MTU3MjAzODExMzIxMn0.xG0vh_Gko2Vc8Mk8K57jaIx4FuDrFYYX6oWBxH62CTc"
        vectorlyPlayer.load(videoId,  token,true, object: OnLoadListener {

            override fun onLoaded(vectorlyPlayer: VectorlyPlayer) {

                println("Video loaded!")

            }

        })
    }

    private fun setupAllListeners() {

        /*
        vectorlyPlayer.setOnErrorListener(object: OnErrorListener {

            override fun onError(vectorlyPlayer: VectorlyPlayer, e: LRNPlayerException) {

            }

        })

        vectorlyPlayer.setOnMetadataLoadedListener(object: OnMetadataLoadedListener {

            override fun onMetadataLoaded(vectorlyPlayer: VectorlyPlayer, metadata: VideoMetadata) {

            }

        })

        vectorlyPlayer.setOnDownloadListener(object: OnDownloadProgressListener {

            override fun onDownloadProgress(vectorlyPlayer: VectorlyPlayer, progressPercent: Int) {

            }

        })

        vectorlyPlayer.setOnCompletionListener(object: OnPlaybackCompletionListener {

            override fun onPlaybackCompletion(vectorlyPlayer: VectorlyPlayer) {

            }

        })

        vectorlyPlayer.setOnFullScreenToggledListener(object: OnFullScreenToggledListener {

            override fun onFullScreenToggled(vectorlyPlayer: VectorlyPlayer) {
                FullScreenUtils.toggleOrientation(this@MainActivity)
            }

        })

         */
    }


    /*
    @Override
    override fun onPause() {
        super.onPause()
        // When the activity is paused, also pause the video playback
        vectorlyPlayer.pause()
    }*/

    @Override
    override fun onDestroy() {
        super.onDestroy()
        // When the activity is being destroyed or the player is no longer needed, call the release
        // function to free up system resources
        vectorlyPlayer.release()
    }

}
