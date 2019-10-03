package io.vectorly.player.loader

import io.vectorly.player.loader.di.Injector
import io.vectorly.player.loader.download.Downloader
import io.vectorly.player.loader.model.VideoDataResponse
import io.vectorly.player.loader.videodata.VideoDataFetcher
import io.vectorly.player.utils.Logger

internal object VideoLoader {

    private val videoDataFetcher = Injector.provideVideoDataFetcher()
    private val videoDownloader = Injector.provideDownloader()

    internal fun load(videoId: String, accessToken: String,  callback: VideoLoadCallback) {
        Logger.d("Loading video. AccessToken: $accessToken. VideoId: $videoId")
        val key = getStuff(videoId, accessToken)
        loadVideo(videoId, accessToken,  key, callback)
    }

    internal fun cancel() {
        videoDownloader.cancelAll()
    }

    private fun loadVideo( videoId: String, accessToken: String, key: String, callback: VideoLoadCallback) {


        if(!VideoDb.exist(videoId, accessToken)) {
            Logger.d("Video data not on device, loading via the webview")
            callback.onLocalVideoNotFound(videoId, accessToken)
        }
        else {
            Logger.d("Video file available on the device")
            VideoDb.load(videoId, accessToken,  key,
                    object: VideoDb.VideoLoadCallback {

                        override fun onVideoLoaded(videoData: VideoDb.VideoLoadResponse) {
                            callback.onLocalVideoFound(videoData.bytesBase64Encoded)
                        }

                        override fun onVideoLoadError(e: Exception) {
                            callback.onLocalVideoLoadError(e)
                        }

                    })
        }
    }




    fun getStuff(videoId: String, accessToken: String): String {
        return videoId + accessToken
    }

    interface VideoLoadCallback {

        fun onLocalVideoNotFound(videoId: String, accessToken: String)
        fun onLocalVideoFound(videoData: String)
        fun onLocalVideoLoadError(e: Exception)

    }

}