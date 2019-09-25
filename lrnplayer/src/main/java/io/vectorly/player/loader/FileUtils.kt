package io.vectorly.player.loader

import android.os.Environment
import java.io.File

internal class FileUtils {

    internal fun getVideoFile(accessToken: String, videoId: String): File {
        val videoDir = File(getVideoDirectory(accessToken))
        // Create video directory if it doesn't exist
        if (!videoDir.exists())
            videoDir.mkdirs()

        return File(videoDir, videoId)
    }

    private fun getVideoDirectory(accessToken: String): String {
        // Return a hidden directory (because of the .) based on the accessToken in the external storage
        return getExternalStorageDirectoryAbsolutePath() + File.separator + "." + accessToken  +
                File.separator
    }

    private fun getExternalStorageDirectoryAbsolutePath(): String {
        return Environment.getExternalStorageDirectory().absolutePath
    }

}