package io.dotlearn.lrnplayer.error

enum class ErrorCode(val code: Int, val message: String) {

    NOT_PREPARED(-1, "LRNPlayerView is not prepared. Call prepare before using vectorized video playback"),
    OFFLINE(-2, "The device is currently offline")

}