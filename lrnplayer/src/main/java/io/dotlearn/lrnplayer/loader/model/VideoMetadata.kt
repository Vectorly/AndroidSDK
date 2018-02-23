package io.dotlearn.lrnplayer.loader.model

import com.google.gson.annotations.SerializedName

data class VideoMetadata(
        @SerializedName("_id")
        val id: String,
        @SerializedName("__v")
        val V: Int,
        @SerializedName("public")
        val isPublic: Boolean,
        @SerializedName("default_size")
        val defaultSize: String,
        val thumbnail: String,
        val module: String,
        val name: String,
        val description: String,
        val published: Boolean
)
