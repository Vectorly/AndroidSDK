package io.dotlearn.lrnplayer.loader.videodata

import io.dotlearn.lrnplayer.loader.model.VideoDataRequest
import io.dotlearn.lrnplayer.loader.model.VideoDataResponse
import io.dotlearn.lrnplayer.utils.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

internal class VideoDataFetcher(private val videoService: VideoService) {

    internal fun fetch(accessToken: String, videoId: String, callback: VideoDataFetchCallback) {
        fetchData(accessToken, videoId, callback)
    }

    private fun fetchData(accessToken: String, videoId: String, callback: VideoDataFetchCallback) {
        Logger.d("Fetching video data. AccessToken: $accessToken. VideoId: $videoId")
        videoService.serveVideo(VideoDataRequest(accessToken, videoId, "mp3_24"))
                .enqueue(object: Callback<VideoDataResponse> {

                    override fun onFailure(call: Call<VideoDataResponse>?, t: Throwable?) {
                        Logger.e("Error fetching video data. Error: $t")
                        callback.onVideoDataFetchError(Exception(t))
                    }

                    override fun onResponse(call: Call<VideoDataResponse>,
                                            response: Response<VideoDataResponse>) {
                        Logger.d("On video data response received")
                        val videoDataResponse = response.body()

                        if(!response.isSuccessful || videoDataResponse == null) {
                            val exception = HttpException(response)
                            Logger.e("Response was not successful. Error: $exception")
                            callback.onVideoDataFetchError(exception)
                            return
                        }

                        Logger.d("VideoDataResponse: $videoDataResponse")
                        callback.onVideoDataFetched(videoDataResponse)
                    }

                })
    }

    internal interface VideoDataFetchCallback {

        fun onVideoDataFetched(videoData: VideoDataResponse)
        fun onVideoDataFetchError(e: Exception)

    }

}