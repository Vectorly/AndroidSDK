package io.dotlearn.lrnplayer.loader

import io.dotlearn.lrnplayer.loader.di.Injector
import io.dotlearn.lrnplayer.loader.download.Downloader
import io.dotlearn.lrnplayer.loader.model.VideoDataResponse
import io.dotlearn.lrnplayer.loader.model.VideoMetadata
import io.dotlearn.lrnplayer.loader.videodata.VideoDataFetcher
import io.dotlearn.lrnplayer.utils.Logger

internal object VideoLoader {

    private val videoDataFetcher = Injector.provideVideoDataFetcher()
    private val videoDownloader = Injector.provideDownloader()

    internal fun load(accessToken: String, videoId: String, callback: VideoLoadCallback) {
        Logger.d("Loading video. AccessToken: $accessToken. VideoId: $videoId")
        callback.onVideoLoadStarted()
        val key = getStuff(accessToken, videoId)
        loadVideo(accessToken, videoId, key, callback)
    }

    internal fun cancel() {
        videoDownloader.cancelAll()
    }

    private fun loadVideo(accessToken: String, videoId: String, key: String, callback: VideoLoadCallback) {
        if(!VideoDb.exist(accessToken, videoId)) {
            Logger.d("Video metadata and file is available on device")
            fetchVideoData(accessToken, videoId, key, callback)
        }
        else {
            Logger.d("Video metadata or file is not available on the device")
            VideoDb.load(accessToken, videoId, key,
                    object: VideoDb.VideoLoadCallback {

                        override fun onVideoLoaded(videoData: VideoDb.VideoLoadResponse) {
                            callback.onVideoLoaded(videoData.metadata, videoData.bytesBase64Encoded)
                        }

                        override fun onVideoLoadError(e: Exception) {
                            callback.onVideoLoadError(e)
                        }

                    })
        }
    }

    private fun fetchVideoData(accessToken: String, videoId: String, key: String, callback: VideoLoadCallback) {
        Logger.d("Fetching video data")
        videoDataFetcher.fetch(accessToken, videoId,
                object: VideoDataFetcher.VideoDataFetchCallback {

                    override fun onVideoDataFetched(videoData: VideoDataResponse) {
                        VideoDb.saveMetadata(accessToken, videoId, videoData.metaData)
                        downloadVideo(accessToken, videoId, videoData, key, callback)
                    }

                    override fun onVideoDataFetchError(e: Exception) {
                        callback.onVideoLoadError(e)
                    }

                })
    }

    private fun downloadVideo(accessToken: String, videoId: String, videoData: VideoDataResponse,
                              key: String, callback: VideoLoadCallback) {
        videoDownloader.download(videoData.url, VideoDb.getFile(accessToken, videoId), videoId, key,
                object: Downloader.DownloadCallback {

                    override fun onDownloadStarted(downloadTag: String) {
                        Logger.d("Download started. Tag: $downloadTag")
                    }

                    override fun onDownloadProgressUpdate(downloadTag: String, bytesTransferred: Long, totalBytes: Long) {
                        Logger.d("Download progress update. Tag: $downloadTag")
                        callback.onVideoLoadProgress(bytesTransferred, totalBytes)
                    }

                    override fun onDownloadError(downloadTag: String, e: Exception) {
                        Logger.d("Download error. Tag: $downloadTag")
                        callback.onVideoLoadError(e)
                    }

                    override fun onDownloadCompleted(downloadTag: String) {
                        Logger.d("Download completed. Tag: $downloadTag")
                        loadVideo(accessToken, videoId, key, callback)
                    }

                })
    }

    private fun getStuff(accessToken: String, videoId: String): String {
        return accessToken + videoId
    }

    interface VideoLoadCallback {

        fun onVideoLoadStarted()
        fun onVideoLoadProgress(bytesTransferred: Long, totalBytes: Long)
        fun onVideoLoaded(metadata: VideoMetadata, videoDataBase64Encoded: String)
        fun onVideoLoadError(e: Exception)

    }

}