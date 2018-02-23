package org.dotlearn.lrncurriculum.data.local

import io.paperdb.Paper
import org.dotlearn.lrncurriculum.di.Injector
import org.dotlearn.lrncurriculum.models.Section

private const val KEY_SECTIONS = "KEY_SECTIONS_"

internal class SectionDb {

    private val ioUtils = Injector.provideIoUtils()

    internal fun saveSections(courseId: String, sections: List<Section>) {
        Paper.book().write(getKey(courseId), sections)
    }

    internal fun getSections(courseId: String): List<Section>? {
        return Paper.book().read(getKey(courseId), null)
    }

    private fun getKey(courseId: String) = ioUtils.md5(KEY_SECTIONS + courseId)

}