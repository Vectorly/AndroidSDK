package org.dotlearn.lrncurriculum.fetcher

import android.util.LruCache
import org.dotlearn.lrncurriculum.CurriculumService
import org.dotlearn.lrncurriculum.models.Course

private const val KEY_COURSES = "KEY_COURSES"

internal class CourseFetcher(private val service: CurriculumService,
                             private val cache: LruCache<String, List<Course>>,
                             private val callback: CourseCallback) {

    fun fetchCourses() {
        if (!localFetch()) {
            remoteFetch()
        }
    }

    private fun localFetch(): Boolean {
        val courses = cache.get(getCacheKey())
        if (courses != null) {
            callback.onCoursesLoaded(courses)
            return true
        }

        return false
    }

    private fun remoteFetch() {
        service.getCourses().enqueue(object: RemoteCallback<List<Course>>() {

            override fun onSuccess(response: List<Course>) {
                cache.put(getCacheKey(), response)
                callback.onCoursesLoaded(response)
            }

            override fun onFailed(t: Throwable) {
                callback.onCoursesLoadFailed(t)
            }

        })
    }

    private fun getCacheKey() = "courses_" + KEY_COURSES

    interface CourseCallback {

        fun onCoursesLoaded(courses: List<Course>)
        fun onCoursesLoadFailed(t: Throwable)

    }

}