package io.dotlearn.lrnplayer.loader.model

import com.google.gson.annotations.SerializedName

data class VideoDataResponse(val size: VideoSize,
                                      @SerializedName("meta_data")
                                      val metaData: VideoMetadata,
                                      val url: String?)