package org.dotlearn.lrncurriculum.data.local

import io.paperdb.Paper
import org.dotlearn.lrncurriculum.models.Course

private const val KEY_COURSES = "KEY_COURSES"

internal class CourseDb {

    internal fun saveCourses(courses: List<Course>) {
        Paper.book().write(KEY_COURSES, courses)
    }

    internal fun getCourses(): List<Course>? {
        return Paper.book().read(KEY_COURSES, null)
    }

}