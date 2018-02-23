package org.dotlearn.lrncurriculum.data.remote

import org.dotlearn.lrncurriculum.CurriculumService
import org.dotlearn.lrncurriculum.models.Video
import retrofit2.HttpException

internal class SearchLoader(private val service: CurriculumService) {

    internal fun loadVideosWithName(searchTerm: String, courseId: String): List<Video> {
        val response = service.getVideosWithName(searchTerm, courseId).execute()
        return LoaderUtils.getResponseBody<List<Video>>(response)
    }

}