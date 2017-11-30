package io.dotlearn.vectorizedvideo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.dotlearn.lrnplayer.LRNPlayerView
import io.dotlearn.lrnplayer.listener.OnPreparedListener

class MainActivity : AppCompatActivity() {

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
