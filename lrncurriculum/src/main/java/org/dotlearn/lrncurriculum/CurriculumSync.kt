package org.dotlearn.lrncurriculum

import org.dotlearn.lrncurriculum.data.local.CourseDb
import org.dotlearn.lrncurriculum.data.local.LessonDb
import org.dotlearn.lrncurriculum.data.local.SectionDb
import org.dotlearn.lrncurriculum.data.local.VideoDb
import org.dotlearn.lrncurriculum.data.remote.CourseLoader
import org.dotlearn.lrncurriculum.data.remote.LessonLoader
import org.dotlearn.lrncurriculum.data.remote.SectionLoader
import org.dotlearn.lrncurriculum.data.remote.VideoLoader
import org.dotlearn.lrncurriculum.di.Injector
import org.dotlearn.lrncurriculum.models.Course
import org.dotlearn.lrncurriculum.models.Lesson
import org.dotlearn.lrncurriculum.models.Section
import org.dotlearn.lrncurriculum.models.Video

@Suppress("JoinDeclarationAndAssignment")
object CurriculumSync {

    private val courseDb: CourseDb
    private val courseLoader: CourseLoader
    private val sectionDb: SectionDb
    private val sectionLoader: SectionLoader
    private val lessonDb: LessonDb
    private val lessonLoader: LessonLoader
    private val videoDb: VideoDb
    private val videoLoader: VideoLoader

    init {
        courseDb = Injector.provideCourseDb()
        sectionDb = Injector.provideSectionDb()
        lessonDb = Injector.provideLessonDb()
        videoDb = Injector.provideVideoDb()

        courseLoader = Injector.provideCourseLoader()
        sectionLoader = Injector.provideSectionLoader()
        lessonLoader = Injector.provideLessonLoader()
        videoLoader = Injector.provideVideoLoader()
    }
    
    private fun syncAll() {
        val courses = syncCourses()

        for(course in courses) {
            val sections = syncSections(course.id)

            for(section in sections) {
                val lessons = syncLessons(section.id)

                for(lesson in lessons) {
                    syncVideos(lesson.id)
                }
            }
        }
    }

    fun syncCourses(): List<Course> {
        val remoteCourses = courseLoader.loadAllCourses()
        courseDb.saveCourses(remoteCourses)

        return remoteCourses
    }
    
    fun syncSections(courseId: String): List<Section> {
        val remoteSections = sectionLoader.loadSectionsInCourse(courseId)
        sectionDb.saveSections(courseId, remoteSections)

        return remoteSections
    }
    
    fun syncLessons(sectionId: String): List<Lesson> {
        val remoteLessons = lessonLoader.loadLessonInSection(sectionId)
        lessonDb.saveLessons(sectionId, remoteLessons)

        return remoteLessons
    }
    
    fun syncVideos(lessonId: String): List<Video> {
        val remoteVideos = videoLoader.loadVideosInLesson(lessonId)
        videoDb.saveVideos(lessonId, remoteVideos)

        return remoteVideos
    }
    
}