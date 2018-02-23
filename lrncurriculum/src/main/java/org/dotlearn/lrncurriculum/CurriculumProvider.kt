package org.dotlearn.lrncurriculum

import android.content.Context
import io.paperdb.Paper
import org.dotlearn.lrncurriculum.data.local.CourseDb
import org.dotlearn.lrncurriculum.data.local.LessonDb
import org.dotlearn.lrncurriculum.data.local.SectionDb
import org.dotlearn.lrncurriculum.data.local.VideoDb
import org.dotlearn.lrncurriculum.data.remote.*
import org.dotlearn.lrncurriculum.di.Injector
import org.dotlearn.lrncurriculum.models.Course
import org.dotlearn.lrncurriculum.models.Lesson
import org.dotlearn.lrncurriculum.models.Section
import org.dotlearn.lrncurriculum.models.Video

@Suppress("JoinDeclarationAndAssignment")
object CurriculumProvider {

    private lateinit var courseDb: CourseDb
    private lateinit var courseLoader: CourseLoader
    private lateinit var sectionDb: SectionDb
    private lateinit var sectionLoader: SectionLoader
    private lateinit var lessonDb: LessonDb
    private lateinit var lessonLoader: LessonLoader
    private lateinit var videoDb: VideoDb
    private lateinit var videoLoader: VideoLoader
    private lateinit var searchLoader: SearchLoader

    fun init(context: Context) {
        initialize(context)
    }

    private fun initialize(context: Context) {
        Paper.init(context)

        courseDb = Injector.provideCourseDb()
        sectionDb = Injector.provideSectionDb()
        lessonDb = Injector.provideLessonDb()
        videoDb = Injector.provideVideoDb()

        courseLoader = Injector.provideCourseLoader()
        sectionLoader = Injector.provideSectionLoader()
        lessonLoader = Injector.provideLessonLoader()
        videoLoader = Injector.provideVideoLoader()
        searchLoader = Injector.provideSearchLoader()
    }

    fun getCourses(): List<Course> {
        return getAllCourses()
    }

    private fun getAllCourses(): List<Course> {
        val localCourses = courseDb.getCourses()

        return if(localCourses == null) {
            val remoteCourses = courseLoader.loadAllCourses()
            courseDb.saveCourses(remoteCourses)

            remoteCourses
        }
        else {
            localCourses
        }
    }

    fun getSections(courseId: String): List<Section> {
        return getSectionsInCourse(courseId)
    }

    private fun getSectionsInCourse(courseId: String): List<Section> {
        val localSections = sectionDb.getSections(courseId)

        return if(localSections == null) {
            val remoteSections = sectionLoader.loadSectionsInCourse(courseId)
            sectionDb.saveSections(courseId, remoteSections)

            remoteSections
        }
        else {
            localSections
        }
    }

    fun getLessons(sectionId: String): List<Lesson> {
        return getLessonsInSection(sectionId)
    }

    private fun getLessonsInSection(sectionId: String): List<Lesson> {
        val localLessons = lessonDb.getLessons(sectionId)

        return if(localLessons == null) {
            val remoteLessons = lessonLoader.loadLessonInSection(sectionId)
            lessonDb.saveLessons(sectionId, remoteLessons)

            remoteLessons
        }
        else {
            localLessons
        }
    }

    fun getVideos(lessonId: String): List<Video> {
        return getVideosInLesson(lessonId)
    }

    private fun getVideosInLesson(lessonId: String): List<Video> {
        val localVideos = videoDb.getVideos(lessonId)

        return if(localVideos == null) {
            val remoteVideos = videoLoader.loadVideosInLesson(lessonId)
            videoDb.saveVideos(lessonId, remoteVideos)

            remoteVideos
        }
        else {
            localVideos
        }
    }

    fun searchVideos(searchTerm: String, courseId: String): List<Video> {
        return searchLoader.loadVideosWithName(searchTerm, courseId)
    }

}