package org.dotlearn.lrncurriculum.data.local

import io.paperdb.Paper
import org.dotlearn.lrncurriculum.di.Injector
import org.dotlearn.lrncurriculum.models.Video

private const val KEY_VIDEOS = "KEY_VIDEOS_"

internal class VideoDb {

    private val ioUtils = Injector.provideIoUtils()

    fun saveVideos(lessonId: String, videos: List<Video>) {
        Paper.book().write(getKey(lessonId), videos)
    }

    fun getVideos(lessonId: String): List<Video>? {
        return Paper.book().read(getKey(lessonId), null)
    }

    private fun getKey(lessonId: String) = ioUtils.md5(KEY_VIDEOS + lessonId)

}