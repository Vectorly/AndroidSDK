package io.dotlearn.lrnplayer.loader.model

import android.support.annotation.Keep

@Keep
data class VideoDataRequest(@Keep val access_token: String, @Keep val video_id: String)