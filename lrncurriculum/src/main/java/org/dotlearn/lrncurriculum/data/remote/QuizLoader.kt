package org.dotlearn.lrncurriculum.data.remote

import org.dotlearn.lrncurriculum.CurriculumService
import org.dotlearn.lrncurriculum.models.Quiz

internal class QuizLoader(private val service: CurriculumService) {

    internal fun loadQuizzesInLesson(lessonId: String): List<Quiz> {
        val response = service.getQuizzesInLesson(lessonId).execute()
        return LoaderUtils.getResponseBody<List<Quiz>>(response)
    }

}