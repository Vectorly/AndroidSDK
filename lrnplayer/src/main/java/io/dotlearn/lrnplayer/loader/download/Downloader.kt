package io.dotlearn.lrnplayer.loader.download

import android.os.AsyncTask
import io.dotlearn.lrnplayer.utils.IoUtils
import io.dotlearn.lrnplayer.utils.Logger
import okhttp3.OkHttpClient
import okhttp3.Request
import org.encryptor4j.Encryptor
import java.io.*


internal class Downloader(private val okHttpClient: OkHttpClient,
                          private val ioUtils: IoUtils) {

    private val tasks: HashMap<String, DownloadTask> = HashMap()

    internal fun download(downloadUrl: String?, destFile: File, downloadTag: String, key: String,
                          callback: DownloadCallback) {
        downloadVideo(downloadUrl, destFile, downloadTag, key, callback)
    }

    private fun downloadVideo(downloadUrl: String?, destFile: File, downloadTag: String, key: String,
                          callback: DownloadCallback) {
        Logger.d("Download url: $downloadUrl. DestFile: ${destFile.absolutePath}. Tag: $downloadTag")
        val downloadRequest = DownloadRequest(downloadUrl, destFile, downloadTag, key)
        val downloadTask = DownloadTask(downloadRequest, callback, okHttpClient, ioUtils)

        tasks[downloadTag] = downloadTask
        downloadTask.execute()
    }

    internal fun cancel(downloadTag: String) {
        Logger.d("Cancelling task with tag: $downloadTag")
        tasks[downloadTag]?.cancel(true)
        tasks.remove(downloadTag)
    }

    internal fun cancelAll() {
        Logger.d("Cancelling all tasks")
        tasks.forEach {
            Logger.d("Cancelling task with tag: ${it.key}")
            it.value.cancel(true)
        }

        tasks.clear()
    }

    private class DownloadTask(private val downloadRequest: DownloadRequest,
                               private val downloadCallback: DownloadCallback,
                               private val okHttpClient: OkHttpClient,
                               private val ioUtils: IoUtils): AsyncTask<Void, Pair<Long, Long>, Exception>() {

        override fun onPreExecute() {
            Logger.d("Starting video download")
            downloadCallback.onDownloadStarted(downloadRequest.requestTag)
        }

        override fun doInBackground(vararg params: Void): Exception? {
            return dInBackground()
        }

        private fun dInBackground(): Exception? {
            Logger.d("Downloading in the background")

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                val secretKey = ioUtils.getThing(downloadRequest.key)
                val encryptor = Encryptor(secretKey, "AES/CTR/NoPadding", 16)

                if(downloadRequest.downloadUrl == null) {
                    Logger.e("Error downloading video. Video downloadUrl is null. Url: %s" +
                            downloadRequest.downloadUrl)
                    return NullPointerException("Video download url is null")
                }

                val request = Request.Builder().url(downloadRequest.downloadUrl).build()
                val response = okHttpClient.newCall(request).execute()

                inputStream = response.body()!!.byteStream()
                outputStream = encryptor.wrapOutputStream(FileOutputStream(downloadRequest.destFile))

                var totalCount = response.body()!!.contentLength()
                if(totalCount == -1L) {
                    totalCount = inputStream!!.available().toLong()
                }

                val buffer = ByteArray(8 * 1024)
                var len = -1
                var readLen = 0L

                while ({ len = inputStream.read(buffer); len }() != -1) {
                    outputStream.write(buffer, 0, len)
                    readLen += len.toLong()
                    publishProgress(Pair(readLen, totalCount))
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
                Logger.e("Error downloading video: $e")
                return e
            }
            finally {
                Logger.d("Closing streams")
                ioUtils.closeQuietly(inputStream)
                ioUtils.closeQuietly(outputStream)
            }

            return null
        }

        override fun onProgressUpdate(vararg numbers: Pair<Long, Long>) {
            val bytesTransferred = numbers[0].first
            val totalBytes = numbers[0].second
            downloadCallback.onDownloadProgressUpdate(downloadRequest.requestTag, bytesTransferred, totalBytes)
            Logger.d("On progress update. Transferred: $bytesTransferred. Total: $totalBytes")
        }

        override fun onPostExecute(e: Exception?) {
            if (e == null) {
                Logger.d("Download completed successfully")
                downloadCallback.onDownloadCompleted(downloadRequest.requestTag)
            } else {
                Logger.d("Download completed with error")
                downloadCallback.onDownloadError(downloadRequest.requestTag, e)
            }
        }
    }

    internal interface DownloadCallback {

        fun onDownloadStarted(downloadTag: String)
        fun onDownloadProgressUpdate(downloadTag: String, bytesTransferred: Long, totalBytes: Long)
        fun onDownloadError(downloadTag: String, e: Exception)
        fun onDownloadCompleted(downloadTag: String)

    }

}