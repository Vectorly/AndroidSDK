package org.dotlearn.lrncurriculum.fetcher

import android.util.LruCache
import org.dotlearn.lrncurriculum.CurriculumService
import org.dotlearn.lrncurriculum.models.Video

internal class CourseVideoFetcher(private val service: CurriculumService,
                                  private val cache: LruCache<String, List<Video>>,
                                  private val callback: VideoCallback) {

    fun fetchVideos(courseId: String) {
        if (!localFetch(courseId)) {
            remoteFetch(courseId)
        }
    }

    private fun localFetch(courseId: String): Boolean {
        val videos = cache.get(getCacheKey(courseId))
        if (videos != null) {
            callback.onVideosLoaded(courseId, videos)
            return true
        }

        return false
    }

    private fun remoteFetch(courseId: String) {
        service.getVideosInCourse(courseId).enqueue(object: RemoteCallback<List<Video>>() {

            override fun onSuccess(response: List<Video>) {
                cache.put(getCacheKey(courseId), response)
                callback.onVideosLoaded(courseId, response)
            }

            override fun onFailed(t: Throwable) {
                callback.onVideosLoadFailed(courseId, t)
            }

        })
    }

    private fun getCacheKey(courseId: String) = "course_videos_" + courseId

    interface VideoCallback {

        fun onVideosLoaded(courseId: String, videos: List<Video>)
        fun onVideosLoadFailed(courseId: String, t: Throwable)

    }

}