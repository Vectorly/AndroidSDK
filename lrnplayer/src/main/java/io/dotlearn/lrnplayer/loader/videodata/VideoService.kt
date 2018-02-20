package io.dotlearn.lrnplayer.loader.videodata

import io.dotlearn.lrnplayer.loader.model.VideoDataRequest
import io.dotlearn.lrnplayer.loader.model.VideoDataResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

internal const val BASE_URL = "https://api.dotlearn.io/"

internal interface VideoService {

    @POST("serve/video")
    fun serveVideo(@Body videoRequest: VideoDataRequest): Call<VideoDataResponse>

}