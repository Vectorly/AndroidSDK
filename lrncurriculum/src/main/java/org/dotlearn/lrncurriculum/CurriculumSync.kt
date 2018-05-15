package org.dotlearn.lrncurriculum

import org.dotlearn.lrncurriculum.data.local.*
import org.dotlearn.lrncurriculum.data.remote.*
import org.dotlearn.lrncurriculum.di.Injector
import org.dotlearn.lrncurriculum.models.*

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
    private val quizDb: QuizDb
    private val quizLoader: QuizLoader

    init {
        courseDb = Injector.provideCourseDb()
        sectionDb = Injector.provideSectionDb()
        lessonDb = Injector.provideLessonDb()
        videoDb = Injector.provideVideoDb()
        quizDb = Injector.provideQuizDb()

        courseLoader = Injector.provideCourseLoader()
        sectionLoader = Injector.provideSectionLoader()
        lessonLoader = Injector.provideLessonLoader()
        videoLoader = Injector.provideVideoLoader()
        quizLoader = Injector.provideQuizLoader()
    }
    
    fun shallowSyncAll(): HashSet<String> {
        return syncCoursesSectionsAndLessons()
    }

    fun deepSyncAll() {
        val lessonIdSet = shallowSyncAll()

        for(lessonId in lessonIdSet) {
            syncVideos(lessonId)
            syncQuizzes(lessonId)
        }
    }

    private fun syncCoursesSectionsAndLessons(): HashSet<String> {
        val lessonIdSet = HashSet<String>()
        val courses = syncCourses()

        for(course in courses) {
            val sections = syncSections(course.id)

            for(section in sections) {
                val lessons = syncLessons(section.id)

                for(lesson in lessons) {
                    lessonIdSet.add(lesson.id)
                }
            }
        }

        return lessonIdSet
    }

    private fun syncCourses(): List<Course> {
        val remoteCourses = courseLoader.loadAllCourses()
        courseDb.saveCourses(remoteCourses)

        return remoteCourses
    }
    
    private fun syncSections(courseId: String): List<Section> {
        val remoteSections = sectionLoader.loadSectionsInCourse(courseId)
        sectionDb.saveSections(courseId, remoteSections)

        return remoteSections
    }
    
    private fun syncLessons(sectionId: String): List<Lesson> {
        val remoteLessons = lessonLoader.loadLessonInSection(sectionId)
        lessonDb.saveLessons(sectionId, remoteLessons)

        return remoteLessons
    }
    
    private fun syncVideos(lessonId: String): List<Video> {
        val remoteVideos = videoLoader.loadVideosInLesson(lessonId)
        videoDb.saveVideos(lessonId, remoteVideos)

        return remoteVideos
    }

    private fun syncQuizzes(lessonId: String): List<Quiz> {
        val remoteQuizzes = quizLoader.loadQuizzesInLesson(lessonId)
        quizDb.saveQuizzes(lessonId, remoteQuizzes)

        return remoteQuizzes
    }
    
}