package org.dotlearn.lrncurriculum.fetcher

import android.util.LruCache
import org.dotlearn.lrncurriculum.CurriculumService
import org.dotlearn.lrncurriculum.models.Lesson

internal class SectionLessonFetcher(private val service: CurriculumService,
                                    private val cache: LruCache<String, List<Lesson>>,
                                    private val callback: LessonCallback) {

    fun fetchLessons(sectionId: String) {
        if (!localFetch(sectionId)) {
            remoteFetch(sectionId)
        }
    }

    private fun localFetch(sectionId: String): Boolean {
        val lessons = cache.get(getCacheKey(sectionId))
        if (lessons != null) {
            callback.onLessonsLoaded(sectionId, lessons)
            return true
        }

        return false
    }

    private fun remoteFetch(sectionId: String) {
        service.getLessonsInSection(sectionId).enqueue(object: RemoteCallback<List<Lesson>>() {

            override fun onSuccess(response: List<Lesson>) {
                cache.put(getCacheKey(sectionId), response)
                callback.onLessonsLoaded(sectionId, response)
            }

            override fun onFailed(t: Throwable) {
                callback.onLessonsLoadFailed(sectionId, t)
            }

        })
    }

    private fun getCacheKey(sectionId: String) = "section_lessons_" + sectionId

    interface LessonCallback {

        fun onLessonsLoaded(sectionId: String, lessons: List<Lesson>)
        fun onLessonsLoadFailed(sectionId: String, t: Throwable)

    }

}