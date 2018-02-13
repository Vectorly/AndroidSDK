package org.dotlearn.lrncurriculum.data.remote

import org.dotlearn.lrncurriculum.CurriculumService
import org.dotlearn.lrncurriculum.models.Video
import retrofit2.HttpException

internal class VideoLoader(private val service: CurriculumService) {

    internal fun loadVideosInLesson(lessonId: String): List<Video> {
        val response = service.getVideosInLesson(lessonId).execute()
        return LoaderUtils.getResponseBody<List<Video>>(response)
    }

}