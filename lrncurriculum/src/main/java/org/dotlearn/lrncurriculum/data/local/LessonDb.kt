package org.dotlearn.lrncurriculum.data.local

import io.paperdb.Paper
import org.dotlearn.lrncurriculum.di.Injector
import org.dotlearn.lrncurriculum.models.Lesson

private const val KEY_LESSONS = "KEY_LESSONS_"

internal class LessonDb {

    private val ioUtils = Injector.provideIoUtils()

    internal fun saveLessons(sectionId: String, lessons: List<Lesson>) {
        Paper.book().write(getKey(sectionId), lessons)
    }

    internal fun getLessons(sectionId: String): List<Lesson>? {
        return Paper.book().read(getKey(sectionId), null)
    }

    private fun getKey(sectionId: String) = ioUtils.md5(KEY_LESSONS + sectionId)

}