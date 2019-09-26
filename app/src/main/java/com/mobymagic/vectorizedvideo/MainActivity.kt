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


      //  val accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ2aWRlb19pZCI6IjQ4ZmU2OThlLTIyYjAtNDUyOC05NzIyLTI2ZWQwMGFiNzllZiIsImV4cGlyeSI6MTU3MjAzODExMzIxMn0.xG0vh_Gko2Vc8Mk8K57jaIx4FuDrFYYX6oWBxH62CTc"
        val accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ2aWRlb19pZCI6ImY1YmU1OWMwLWRlMjMtNGIwZC04YTM4LTViNzVhYThjOTZjNyIsImV4cGlyeSI6MTU3MjA2NzUwMTU5NX0.Hu1ZT67XgcRhB5s7Tem3M3zdfBUpWa6StV5hGgjEqkQ";


     //   val accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ2aWRlb19pZCI6IjRlZGMzMjc4LWE5NzUtNDlmMy04M2U5LTU0MTJjMmUxMmNmNyIsImV4cGlyeSI6MTU3MjA3MDE2MjA4MH0.leqRq4KwcdB32s4pl6h6Ym09DjgLtGHmIV5XBoAWkTU";


    //    val videoId = "48fe698e-22b0-4528-9722-26ed00ab79ef";
        val videoId = "f5be59c0-de23-4b0d-8a38-5b75aa8c96c7"

       // val videoId = "4edc3278-a975-49f3-83e9-5412c2e12cf7";

        setupAllListeners()
        setupPlaybackControls()

        loadVideo(videoId, accessToken)


    }




    private fun setupPlaybackControls() {

        setupSeekBarControls()
    }

    private fun setupSeekBarControls() {

    }

    private fun loadVideo(videoId: String, accessToken: String) {

        vectorlyPlayer.load(videoId, accessToken,true, object: OnLoadListener {

            override fun onLoaded(vectorlyPlayer: VectorlyPlayer) {

               // Video Loaded

            }

        })


    }



    private fun downloadVideo(videoId: String, accessToken: String) {

        vectorlyPlayer.download(videoId,  accessToken, object: DownloadListener {

            override fun onDownloadStarted(downloadTag: String) {

                println("Download started");

            }

            override fun onDownloadError(downloadTag: String, e: Exception) {
                println("Download error");
                println("excecption: " + e.stackTrace);
            }

            override fun onDownloadProgressUpdate(downloadTag: String, bytesTransferred: Long, totalBytes: Long) {

                println("Download progress");

            }

            override fun onDownloadCompleted(downloadTag: String) {
                println("Download copleted");
            }



        })
    }


    private fun setupAllListeners() {

        /*
        vectorlyPlayer.setOnErrorListener(object: OnErrorListener {

            override fun onError(vectorlyPlayer: VectorlyPlayer, e: LRNPlayerException) {

            }

        })



        vectorlyPlayer.setOnFullScreenToggledListener(object: OnFullScreenToggledListener {

            override fun onFullScreenToggled(vectorlyPlayer: VectorlyPlayer) {
                FullScreenUtils.toggleOrientation(this@MainActivity)
            }

        })

         */
    }




    @Override
    override fun onDestroy() {
        super.onDestroy()
        // When the activity is being destroyed or the player is no longer needed, call the release
        // function to free up system resources
        vectorlyPlayer.release()
    }

}
