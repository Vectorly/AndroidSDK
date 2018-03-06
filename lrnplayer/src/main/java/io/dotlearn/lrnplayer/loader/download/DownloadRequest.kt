package io.dotlearn.lrnplayer.loader.download

import java.io.File

data class DownloadRequest(val downloadUrl: String, val destFile: File, val requestTag: String,
                                    val key: String)