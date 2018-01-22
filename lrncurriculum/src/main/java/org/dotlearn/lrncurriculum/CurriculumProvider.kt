package org.dotlearn.lrncurriculum

import android.util.LruCache
import org.dotlearn.lrncurriculum.fetcher.*
import org.dotlearn.lrncurriculum.models.Course
import org.dotlearn.lrncurriculum.models.Lesson
import org.dotlearn.lrncurriculum.models.Section
import org.dotlearn.lrncurriculum.models.Video
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("JoinDeclarationAndAssignment")
object CurriculumProvider {

    private val courseCache: LruCache<String, List<Course>>
    private val sectionsCache: LruCache<String, List<Section>>
    private val lessonsCache: LruCache<String, List<Lesson>>
    private val videosCache: LruCache<String, List<Video>>

    private val curriculumService: CurriculumService

    init {
        courseCache = LruCache(8)
        sectionsCache = LruCache(16)
        lessonsCache = LruCache(32)
        videosCache = LruCache(64)

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        curriculumService = retrofit.create(CurriculumService::class.java)
    }

    fun loadCourses(courseCallback: CourseCallback) {
        CourseFetcher(curriculumService, courseCache, object: CourseFetcher.CourseCallback {

            override fun onCoursesLoaded(courses: List<Course>) {
                courseCallback.onCoursesLoaded(courses)
            }

            override fun onCoursesLoadFailed(t: Throwable) {
                courseCallback.onCoursesLoadFailed(t)
            }

        }).fetchCourses()
    }

    fun loadSections(courseId: String, callback: SectionCallback) {
        SectionFetcher(curriculumService, sectionsCache, object: SectionFetcher.SectionCallback {

            override fun onSectionsLoaded(courseId: String, sections: List<Section>) {
                callback.onSectionsLoaded(courseId, sections)
            }

            override fun onSectionsLoadFailed(courseId: String, t: Throwable) {
                callback.onSectionsLoadFailed(courseId, t)
            }

        }).fetchSections(courseId)
    }

    fun loadLessonsInCourse(courseId: String, callback: CourseLessonCallback) {
        CourseLessonFetcher(curriculumService, lessonsCache, object: CourseLessonFetcher.LessonCallback {

            override fun onLessonsLoaded(courseId: String, lessons: List<Lesson>) {
                callback.onLessonsLoaded(courseId, lessons)
            }

            override fun onLessonsLoadFailed(courseId: String, t: Throwable) {
                callback.onLessonsLoadFailed(courseId, t)
            }

        }).fetchLessons(courseId)
    }

    fun loadLessonsInSections(sectionId: String, callback: SectionLessonCallback) {
        SectionLessonFetcher(curriculumService, lessonsCache, object: SectionLessonFetcher.LessonCallback {

            override fun onLessonsLoaded(sectionId: String, lessons: List<Lesson>) {
                callback.onLessonsLoaded(sectionId, lessons)
            }

            override fun onLessonsLoadFailed(sectionId: String, t: Throwable) {
                callback.onLessonsLoadFailed(sectionId, t)
            }

        }).fetchLessons(sectionId)
    }

    fun loadVideosInCourse(courseId: String, callback: CourseVideoCallback) {
        CourseVideoFetcher(curriculumService, videosCache, object: CourseVideoFetcher.VideoCallback {

            override fun onVideosLoaded(courseId: String, videos: List<Video>) {
                callback.onVideosLoaded(courseId, videos)
            }

            override fun onVideosLoadFailed(courseId: String, t: Throwable) {
                callback.onVideosLoadFailed(courseId, t)
            }

        }).fetchVideos(courseId)
    }

    fun loadVideosInSection(sectionId: String, callback: SectionVideoCallback) {
        SectionVideoFetcher(curriculumService, videosCache, object: SectionVideoFetcher.VideoCallback {

            override fun onVideosLoaded(sectionId: String, videos: List<Video>) {
                callback.onVideosLoaded(sectionId, videos)
            }

            override fun onVideosLoadFailed(sectionId: String, t: Throwable) {
                callback.onVideosLoadFailed(sectionId, t)
            }

        }).fetchVideos(sectionId)
    }

    fun loadVideosInLesson(lessonId: String, callback: LessonVideoCallback) {
        LessonVideoFetcher(curriculumService, videosCache, object: LessonVideoFetcher.VideoCallback {

            override fun onVideosLoaded(lessonId: String, videos: List<Video>) {
                callback.onVideosLoaded(lessonId, videos)
            }

            override fun onVideosLoadFailed(lessonId: String, t: Throwable) {
                callback.onVideosLoadFailed(lessonId, t)
            }

        }).fetchVideos(lessonId)
    }

    fun searchForVideos(searchTerm: String, courseId: String, callback: SearchVideoCallback) {
        SearchVideoFetcher(curriculumService, videosCache, object: SearchVideoFetcher.VideoCallback {

            override fun onVideosLoaded(searchTerm: String, courseId: String, videos: List<Video>) {
                callback.onVideosLoaded(searchTerm, courseId, videos)
            }

            override fun onVideosLoadFailed(searchTerm: String, courseId: String, t: Throwable) {
                callback.onVideosLoadFailed(searchTerm, courseId, t)
            }


        }).fetchVideos(searchTerm, courseId)
    }

    interface CourseCallback {

        fun onCoursesLoaded(courses: List<Course>)
        fun onCoursesLoadFailed(t: Throwable)

    }

    interface SectionCallback {

        fun onSectionsLoaded(courseId: String, sections: List<Section>)
        fun onSectionsLoadFailed(courseId: String, t: Throwable)

    }

    interface CourseLessonCallback {

        fun onLessonsLoaded(courseId: String, lessons: List<Lesson>)
        fun onLessonsLoadFailed(courseId: String, t: Throwable)

    }

    interface SectionLessonCallback {

        fun onLessonsLoaded(sectionId: String, lessons: List<Lesson>)
        fun onLessonsLoadFailed(sectionId: String, t: Throwable)

    }

    interface CourseVideoCallback {

        fun onVideosLoaded(courseId: String, videos: List<Video>)
        fun onVideosLoadFailed(courseId: String, t: Throwable)

    }

    interface SectionVideoCallback {

        fun onVideosLoaded(sectionId: String, videos: List<Video>)
        fun onVideosLoadFailed(sectionId: String, t: Throwable)

    }

    interface LessonVideoCallback {

        fun onVideosLoaded(lessonId: String, videos: List<Video>)
        fun onVideosLoadFailed(lessonId: String, t: Throwable)

    }

    interface SearchVideoCallback {

        fun onVideosLoaded(searchTerm: String, courseId: String, videos: List<Video>)
        fun onVideosLoadFailed(searchTerm: String, courseId: String, t: Throwable)

    }

}