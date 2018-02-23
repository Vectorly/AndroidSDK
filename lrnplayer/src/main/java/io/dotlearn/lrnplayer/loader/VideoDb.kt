package io.dotlearn.lrnplayer.loader

import android.os.AsyncTask
import android.util.Base64
import io.dotlearn.lrnplayer.loader.di.Injector
import io.dotlearn.lrnplayer.loader.model.VideoMetadata
import io.dotlearn.lrnplayer.utils.Logger
import io.paperdb.Paper
import org.encryptor4j.Encryptor
import java.io.File
import java.io.FileInputStream
import java.io.IOException

internal object VideoDb {

    private val ioUtils = Injector.provideIoUtils()
    private val fileUtils = Injector.provideFileUtils()

    internal fun getFile(accessToken: String, videoId: String): File {
        return fileUtils.getVideoFile(accessToken, videoId)
    }

    internal fun saveMetadata(accessToken: String, videoId: String, videoMetadata: VideoMetadata) {
        Logger.d("Saving video metadata")
        Paper.book().write(getDbKey(accessToken, videoId), videoMetadata)
    }

    internal fun getMetadata(accessToken: String, videoId: String): VideoMetadata? {
        return Paper.book().read(getDbKey(accessToken, videoId), null)
    }

    internal fun load(accessToken: String, videoId: String, key: String, callback: VideoLoadCallback) {
        val task = VideoLoadTask(accessToken, videoId, key, callback)
        task.execute()
    }

    internal fun exist(accessToken: String, videoId: String): Boolean {
        return doesVideoExist(accessToken, videoId)
    }

    private fun doesVideoExist(accessToken: String, videoId: String): Boolean {
        return getFile(accessToken, videoId).exists() && getMetadata(accessToken, videoId) != null
    }

    private fun getDbKey(accessToken: String, videoId: String) = videoId

    internal class VideoLoadTask(private val accessToken: String,
                                 private val videoId: String,
                                 private val key: String,
                                 private val callback: VideoLoadCallback):
            AsyncTask<Void, Void, VideoLoadResponse>() {

        override fun doInBackground(vararg params: Void?): VideoLoadResponse? {
            return dInBackground()
        }

        private fun dInBackground(): VideoLoadResponse? {
            Logger.d("Loading vectorized video file in the background")
            return try {
                val k = ioUtils.getThing(key)
                val encryptor = Encryptor(k, "AES/CTR/NoPadding", 16)

                val videoFile = fileUtils.getVideoFile(accessToken, videoId)
                val fis = encryptor.wrapInputStream(FileInputStream(videoFile))
                val videoDataBytes = ioUtils.toByteArray(fis)
                val videoDataBase64Encoded = Base64.encodeToString(videoDataBytes, Base64.DEFAULT)
                VideoLoadResponse(getMetadata(accessToken, videoId)!!, videoDataBase64Encoded)
            } catch (e: Exception) {
                e.printStackTrace()
                Logger.e("Error loading video: $e")
                null
            }
        }

        override fun onPostExecute(response: VideoLoadResponse?) {
            if(response != null) {
                Logger.d("VideoLoadTask completed with success. Response: $response")
                callback.onVideoLoaded(response)
            }
            else {
                Logger.e("VideoLoadTask completed with error")
                callback.onVideoLoadError(IOException("Failed to read vectorized video file"))
            }
        }
    }

    internal interface VideoLoadCallback {

        fun onVideoLoaded(videoData: VideoLoadResponse)
        fun onVideoLoadError(e: Exception)

    }

    internal data class VideoLoadResponse(val metadata: VideoMetadata, val bytesBase64Encoded: String)

}