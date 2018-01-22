package org.dotlearn.lrncurriculum.fetcher

import android.util.LruCache
import org.dotlearn.lrncurriculum.CurriculumService
import org.dotlearn.lrncurriculum.models.Video

internal class SearchVideoFetcher(private val service: CurriculumService,
                                  private val cache: LruCache<String, List<Video>>,
                                  private val callback: VideoCallback) {

    fun fetchVideos(searchTerm: String, courseId: String) {
        if (!localFetch(searchTerm, courseId)) {
            remoteFetch(searchTerm, courseId)
        }
    }

    private fun localFetch(searchTerm: String, courseId: String): Boolean {
        val videos = cache.get(getCacheKey(searchTerm, courseId))
        if (videos != null) {
            callback.onVideosLoaded(searchTerm, courseId, videos)
            return true
        }

        return false
    }

    private fun remoteFetch(searchTerm: String, courseId: String) {
        service.getVideosWithName(searchTerm, courseId).enqueue(object: RemoteCallback<List<Video>>() {

            override fun onSuccess(response: List<Video>) {
                cache.put(getCacheKey(searchTerm, courseId), response)
                callback.onVideosLoaded(searchTerm, courseId, response)
            }

            override fun onFailed(t: Throwable) {
                callback.onVideosLoadFailed(searchTerm, courseId, t)
            }

        })
    }

    private fun getCacheKey(searchTerm: String, courseId: String) = "search_videos_" + courseId + searchTerm

    interface VideoCallback {

        fun onVideosLoaded(searchTerm: String, courseId: String, videos: List<Video>)
        fun onVideosLoadFailed(searchTerm: String, courseId: String, t: Throwable)

    }

}