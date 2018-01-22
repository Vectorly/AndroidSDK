package org.dotlearn.lrncurriculum.fetcher

import android.util.LruCache
import org.dotlearn.lrncurriculum.CurriculumService
import org.dotlearn.lrncurriculum.models.Lesson

internal class CourseLessonFetcher(private val service: CurriculumService,
                                   private val cache: LruCache<String, List<Lesson>>,
                                   private val callback: LessonCallback) {

    fun fetchLessons(courseId: String) {
        if (!localFetch(courseId)) {
            remoteFetch(courseId)
        }
    }

    private fun localFetch(courseId: String): Boolean {
        val lessons = cache.get(getCacheKey(courseId))
        if (lessons != null) {
            callback.onLessonsLoaded(courseId, lessons)
            return true
        }

        return false
    }

    private fun remoteFetch(courseId: String) {
        service.getLessonsInCourse(courseId).enqueue(object: RemoteCallback<List<Lesson>>() {

            override fun onSuccess(response: List<Lesson>) {
                cache.put(getCacheKey(courseId), response)
                callback.onLessonsLoaded(courseId, response)
            }

            override fun onFailed(t: Throwable) {
                callback.onLessonsLoadFailed(courseId, t)
            }

        })
    }

    private fun getCacheKey(courseId: String) = "course_lessons_" + courseId

    interface LessonCallback {

        fun onLessonsLoaded(courseId: String, lessons: List<Lesson>)
        fun onLessonsLoadFailed(courseId: String, t: Throwable)

    }

}