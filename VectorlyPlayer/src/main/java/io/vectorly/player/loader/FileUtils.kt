package io.vectorly.player.loader

import android.os.Environment
import java.io.File

internal class FileUtils {

    internal fun getVideoFile( videoId: String, accessToken: String): File {
        val videoDir = File(getVideoDirectory(accessToken))
        // Create video directory if it doesn't exist
        if (!videoDir.exists())
            videoDir.mkdirs()

        val file = File(videoDir, videoId);

        println("Getting file");
        println(file)

        return file
    }

    internal fun deleteVideoFile( videoId: String, accessToken: String): Boolean {

        val file = getVideoFile(videoId, accessToken)

        return  file.delete();
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