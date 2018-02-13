package org.dotlearn.lrncurriculum.data.remote

import org.dotlearn.lrncurriculum.CurriculumService
import org.dotlearn.lrncurriculum.models.Section
import retrofit2.HttpException

internal class SectionLoader(private val service: CurriculumService) {

    internal fun loadSectionsInCourse(courseId: String): List<Section> {
        val response = service.getSectionsInCourse(courseId).execute()
        return LoaderUtils.getResponseBody<List<Section>>(response)
    }

}