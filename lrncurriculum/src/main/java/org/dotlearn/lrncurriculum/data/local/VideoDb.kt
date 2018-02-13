package org.dotlearn.lrncurriculum.data.local

import io.paperdb.Paper
import org.dotlearn.lrncurriculum.models.Video

private const val KEY_VIDEOS = "KEY_VIDEOS_"

internal class VideoDb {

    fun saveVideos(lessonId: String, videos: List<Video>) {
        Paper.book().write(getKey(lessonId), videos)
    }

    fun getVideos(lessonId: String): List<Video>? {
        return Paper.book().read(getKey(lessonId), null)
    }

    private fun getKey(lessonId: String) = KEY_VIDEOS + lessonId

}