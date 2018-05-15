package org.dotlearn.lrncurriculum.data.local

import io.paperdb.Paper
import org.dotlearn.lrncurriculum.di.Injector
import org.dotlearn.lrncurriculum.models.Quiz

private const val KEY_QUIZZES = "KEY_QUIZZES_"

internal class QuizDb {

    private val ioUtils = Injector.provideIoUtils()

    fun saveQuizzes(lessonId: String, quizzes: List<Quiz>) {
        Paper.book().write(getKey(lessonId), quizzes)
    }

    fun getQuizzes(lessonId: String): List<Quiz>? {
        return Paper.book().read(getKey(lessonId), null)
    }

    private fun getKey(lessonId: String) = ioUtils.md5(KEY_QUIZZES + lessonId)

}