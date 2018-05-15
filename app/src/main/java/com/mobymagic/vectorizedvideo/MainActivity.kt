package com.mobymagic.vectorizedvideo

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

        prepareVideo(intent.getStringExtra(EXTRA_VIDEO_ID))
    }

    private fun setupPlaybackControls() {
        videoSkipPreviousButton.setOnClickListener({ prepareVideo("8d474175-8c62-4e6f-9e1f-1997f3e779ca") })
        videoSkipNextButton.setOnClickListener({ prepareVideo("132690b2-baa0-4d36-8d60-15e732d538d3") })
        videoPlayButton.setOnClickListener({ lrnPlayerView.start() })
        videoPauseButton.setOnClickListener({ lrnPlayerView.pause() })

        setupSeekBarControls()
    }

    private fun setupSeekBarControls() {
        videoSeekBar.max = 120 * 1000 // Just 2 minutes
        videoSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, position: Int, fromUser: Boolean) {
                lrnPlayerView.seekTo(position.toLong())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })
    }

    private fun prepareVideo(videoId: String) {
        val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjbGllbnRfaWQiOiJkb3RsZWFybl9pbyIsInR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJzYW5kYm94Ijp0cnVlLCJ0b2tlbl9pZCI6IjlhM2QwODMwLWE5NTMtNGE0Yi1iOWY5LTI3ZjZlODg4ODdmZCJ9.lMRw8ELT5unXc6BxYFQqv2g5Ysg9jWW48zy2WiFwcoo"

        lrnPlayerView.prepare(token, videoId, true, object: OnPreparedListener {

            override fun onPrepared(lrnPlayerView: LRNPlayerView) {
                videoStatusTextView.text = "Video prepared"
            }

        })
    }

    private fun setupAllListeners() {
        lrnPlayerView.setOnErrorListener(object: OnErrorListener {

            override fun onError(lrnPlayerView: LRNPlayerView, e: LRNPlayerException) {
                videoStatusTextView.text = "Error: " + e.message
            }

        })

        lrnPlayerView.setOnMetadataLoadedListener(object: OnMetadataLoadedListener {

            override fun onMetadataLoaded(lrnPlayerView: LRNPlayerView, metadata: VideoMetadata) {
                videoStatusTextView.text = "Metadata: " + metadata.toString()
                videoTitleTextView.text = metadata.name
                videoDescriptionTextView.text = metadata.description
            }

        })

        lrnPlayerView.setOnDownloadListener(object: OnDownloadProgressListener {

            override fun onDownloadProgress(lrnPlayerView: LRNPlayerView, progressPercent: Int) {
                videoStatusTextView.text = "Downloaded: " + progressPercent + "%"
            }

        })

        lrnPlayerView.setOnCompletionListener(object: OnPlaybackCompletionListener {

            override fun onPlaybackCompletion(lrnPlayerView: LRNPlayerView) {
                videoStatusTextView.text = "Video playback completed"
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
