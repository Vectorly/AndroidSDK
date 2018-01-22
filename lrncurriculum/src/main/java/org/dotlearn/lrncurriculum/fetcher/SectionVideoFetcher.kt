package org.dotlearn.lrncurriculum.fetcher

import android.util.LruCache
import org.dotlearn.lrncurriculum.CurriculumService
import org.dotlearn.lrncurriculum.models.Video

internal class SectionVideoFetcher(private val service: CurriculumService,
                                   private val cache: LruCache<String, List<Video>>,
                                   private val callback: VideoCallback) {

    fun fetchVideos(sectionId: String) {
        if (!localFetch(sectionId)) {
            remoteFetch(sectionId)
        }
    }

    private fun localFetch(sectionId: String): Boolean {
        val videos = cache.get(getCacheKey(sectionId))
        if (videos != null) {
            callback.onVideosLoaded(sectionId, videos)
            return true
        }

        return false
    }

    private fun remoteFetch(sectionId: String) {
        service.getVideosInSection(sectionId).enqueue(object: RemoteCallback<List<Video>>() {

            override fun onSuccess(response: List<Video>) {
                cache.put(getCacheKey(sectionId), response)
                callback.onVideosLoaded(sectionId, response)
            }

            override fun onFailed(t: Throwable) {
                callback.onVideosLoadFailed(sectionId, t)
            }

        })
    }

    private fun getCacheKey(sectionId: String) = "section_videos_" + sectionId

    interface VideoCallback {

        fun onVideosLoaded(sectionId: String, videos: List<Video>)
        fun onVideosLoadFailed(sectionId: String, t: Throwable)

    }

}