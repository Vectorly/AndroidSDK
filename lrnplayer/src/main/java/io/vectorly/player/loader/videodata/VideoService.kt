package io.vectorly.player.loader.videodata

import io.vectorly.player.loader.model.VideoDataRequest
import io.vectorly.player.loader.model.VideoDataResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

internal const val BASE_URL = "https://api.dotlearn.io/"

internal interface VideoService {

    @POST("serve/video")
    fun serveVideo(@Body videoRequest: VideoDataRequest): Call<VideoDataResponse>

}