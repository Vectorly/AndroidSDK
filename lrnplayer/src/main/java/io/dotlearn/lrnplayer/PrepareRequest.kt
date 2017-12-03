package io.dotlearn.lrnplayer

import io.dotlearn.lrnplayer.listener.OnPreparedListener

internal class PrepareRequest(val accessToken: String, val videoId: String, val autoStart: Boolean)