package org.dotlearn.lrncurriculum

import org.dotlearn.lrncurriculum.models.Course
import org.dotlearn.lrncurriculum.models.Lesson
import org.dotlearn.lrncurriculum.models.Section
import org.dotlearn.lrncurriculum.models.Video
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

internal const val BASE_URL = "https://api.dotlearn.io/"

internal interface CurriculumService {

    @GET("curriculum/courses/")
    fun getCourses(): Call<List<Course>>

    @GET("curriculum/sections/in/course/{courseId}")
    fun getSectionsInCourse(@Path("courseId") courseId: String): Call<List<Section>>

    @GET("curriculum/lessons/in/course/{courseId}")
    fun getLessonsInCourse(@Path("courseId") courseId: String): Call<List<Lesson>>
    @GET("curriculum/lessons/in/section/{sectionId}")
    fun getLessonsInSection(@Path("sectionId") sectionId: String): Call<List<Lesson>>

    @GET("curriculum/videos/in/course/{courseId}")
    fun getVideosInCourse(@Path("courseId") courseId: String): Call<List<Video>>
    @GET("curriculum/videos/in/section/{sectionId}")
    fun getVideosInSection(@Path("sectionId") sectionId: String): Call<List<Video>>
    @GET("curriculum/videos/in/lesson/{lessonId}")
    fun getVideosInLesson(@Path("lessonId") lessonId: String): Call<List<Video>>
    @GET("/curriculum/search/{searchTerm}/in/course/{courseId}")
    fun getVideosWithName(@Path("searchTerm") searchTerm: String,
                          @Path("courseId") courseId: String): Call<List<Video>>

}