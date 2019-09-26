package io.vectorly.player.loader

import android.os.AsyncTask
import android.util.Base64
import io.vectorly.player.loader.di.Injector
import io.vectorly.player.loader.model.VideoMetadata
import io.vectorly.player.utils.Logger
import io.paperdb.Paper
import org.encryptor4j.Encryptor
import java.io.File
import java.io.FileInputStream
import java.io.IOException

internal object VideoDb {

    private val ioUtils = Injector.provideIoUtils()
    private val fileUtils = Injector.provideFileUtils()

    internal fun getFile(videoId: String, accessToken: String): File {
        return fileUtils.getVideoFile(videoId, accessToken)
    }

    internal fun saveMetadata( videoId: String,accessToken: String, videoMetadata: VideoMetadata) {
        Logger.d("Saving video metadata")
        Paper.book().write(getDbKey( videoId, accessToken), videoMetadata)
    }
/*
    internal fun getMetadata(videoId: String, accessToken: String): VideoMetadata? {
        return Paper.book().read(getDbKey( videoId, accessToken), null)
    }
*/
    internal fun load( videoId: String, accessToken: String, key: String, callback: VideoLoadCallback) {
        val task = VideoLoadTask( videoId, accessToken, key, callback)
        task.execute()
    }

    internal fun exist( videoId: String, accessToken: String): Boolean {
        return doesVideoExist( videoId, accessToken)
    }

    internal fun delete( videoId: String, accessToken: String): Boolean {
        return fileUtils.deleteVideoFile(videoId, accessToken);
    }

    private fun doesVideoExist(videoId: String, accessToken: String): Boolean {

        return getFile(videoId, accessToken).exists()

        //  return getFile(videoId, accessToken).exists() && getMetadata(accessToken, videoId) != null
    }

    @Suppress("UNUSED_PARAMETER")
    private fun getDbKey( videoId: String, accessToken: String) = videoId

    internal class VideoLoadTask(private val videoId: String,
                                private val accessToken: String,
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

                val videoFile = fileUtils.getVideoFile( videoId, accessToken)
                val fis = encryptor.wrapInputStream(FileInputStream(videoFile))
                val videoDataBytes = ioUtils.toByteArray(fis)
                val videoDataBase64Encoded = Base64.encodeToString(videoDataBytes, Base64.DEFAULT)
                 VideoLoadResponse(videoDataBase64Encoded)
            } catch (e: Exception) {
                e.printStackTrace()
                Logger.e("Error loading video: $e")
                null
            }
        }

        override fun onPostExecute(response: VideoLoadResponse?) {
            if(response != null) {
                Logger.d("VideoLoadTask completed with success")
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

    internal data class VideoLoadResponse(val bytesBase64Encoded: String)

}