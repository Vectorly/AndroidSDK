package org.dotlearn.lrncurriculum.fetcher

import android.util.LruCache
import org.dotlearn.lrncurriculum.CurriculumService
import org.dotlearn.lrncurriculum.models.Video

internal class LessonVideoFetcher(private val service: CurriculumService,
                                  private val cache: LruCache<String, List<Video>>,
                                  private val callback: VideoCallback) {

    fun fetchVideos(lessonId: String) {
        if (!localFetch(lessonId)) {
            remoteFetch(lessonId)
        }
    }

    private fun localFetch(lessonId: String): Boolean {
        val videos = cache.get(getCacheKey(lessonId))
        if (videos != null) {
            callback.onVideosLoaded(lessonId, videos)
            return true
        }

        return false
    }

    private fun remoteFetch(lessonId: String) {
        service.getVideosInLesson(lessonId).enqueue(object: RemoteCallback<List<Video>>() {

            override fun onSuccess(response: List<Video>) {
                cache.put(getCacheKey(lessonId), response)
                callback.onVideosLoaded(lessonId, response)
            }

            override fun onFailed(t: Throwable) {
                callback.onVideosLoadFailed(lessonId, t)
            }

        })
    }

    private fun getCacheKey(lessonId: String) = "lesson_videos_" + lessonId

    interface VideoCallback {

        fun onVideosLoaded(lessonId: String, videos: List<Video>)
        fun onVideosLoadFailed(lessonId: String, t: Throwable)

    }

}