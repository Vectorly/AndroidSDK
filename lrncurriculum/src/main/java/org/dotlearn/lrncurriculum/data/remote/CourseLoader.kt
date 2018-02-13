package org.dotlearn.lrncurriculum.data.remote

import org.dotlearn.lrncurriculum.CurriculumService
import org.dotlearn.lrncurriculum.models.Course
import retrofit2.HttpException

internal class CourseLoader(private val service: CurriculumService) {

    /**
     * Loads all the courses from the server
     * @return A list of available courses from the server
     * @throws HttpException if the request was not successful
     */
    internal fun loadAllCourses(): List<Course> {
        return LoaderUtils.getResponseBody<List<Course>>(service.getCourses().execute())
    }

}