package io.dotlearn.lrnplayer.loader.model

import com.google.gson.annotations.SerializedName

internal data class VideoSize(
        @SerializedName("_id")
        val id: String,
        @SerializedName("video_id")
        val videoId: String,
        @SerializedName("__v")
        val V: Int,
        val length: Int,
        val type: String
)
