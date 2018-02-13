package org.dotlearn.lrncurriculum.data.remote

import org.dotlearn.lrncurriculum.CurriculumService
import org.dotlearn.lrncurriculum.models.Lesson
import retrofit2.HttpException

internal class LessonLoader(private val service: CurriculumService) {

    internal fun loadLessonInSection(sectionId: String): List<Lesson> {
        val response = service.getLessonsInSection(sectionId).execute()
        return LoaderUtils.getResponseBody<List<Lesson>>(response)
    }

}