package org.dotlearn.lrncurriculum.fetcher

import android.util.LruCache
import org.dotlearn.lrncurriculum.CurriculumService
import org.dotlearn.lrncurriculum.models.Section

internal class SectionFetcher(private val service: CurriculumService,
                              private val cache: LruCache<String, List<Section>>,
                              private val callback: SectionCallback) {

    fun fetchSections(courseId: String) {
        if (!localFetch(courseId)) {
            remoteFetch(courseId)
        }
    }

    private fun localFetch(courseId: String): Boolean {
        val sections = cache.get(getCacheKey(courseId))
        if (sections != null) {
            callback.onSectionsLoaded(courseId, sections)
            return true
        }

        return false
    }

    private fun remoteFetch(courseId: String) {
        service.getSectionsInCourse(courseId).enqueue(object: RemoteCallback<List<Section>>() {

            override fun onSuccess(response: List<Section>) {
                cache.put(getCacheKey(courseId), response)
                callback.onSectionsLoaded(courseId, response)
            }

            override fun onFailed(t: Throwable) {
                callback.onSectionsLoadFailed(courseId, t)
            }

        })
    }

    private fun getCacheKey(courseId: String) = "section_" + courseId

    interface SectionCallback {

        fun onSectionsLoaded(courseId: String, sections: List<Section>)
        fun onSectionsLoadFailed(courseId: String, t: Throwable)

    }

}