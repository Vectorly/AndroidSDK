package io.dotlearn.vectorizedvideo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_curriculum.*

import org.dotlearn.lrncurriculum.CuriculumProvider
import org.dotlearn.lrncurriculum.models.Course
import org.dotlearn.lrncurriculum.models.Lesson
import org.dotlearn.lrncurriculum.models.Section
import org.dotlearn.lrncurriculum.models.Video

class CurriculumActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_curriculum)

        recyclerView.layoutManager = LinearLayoutManager(this)
        loadCourses()
    }

    private fun loadCourses() {
        progressBar.visibility = View.VISIBLE
        CuriculumProvider.loadCourses(object: CuriculumProvider.CourseCallback {
            override fun onCoursesLoaded(courses: List<Course>) {
                progressBar.visibility = View.GONE

                val adapter = CourseAdapter(courses)
                adapter.setOnItemClickListener { _, _, position -> loadSections(courses[position].id) }
                recyclerView.adapter = adapter
            }

            override fun onCoursesLoadFailed(t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("CurriculumActivity", "Error loading", t)
                Toast.makeText(this@CurriculumActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun loadSections(courseId: String) {
        progressBar.visibility = View.VISIBLE
        CuriculumProvider.loadSections(courseId, object: CuriculumProvider.SectionCallback {

            override fun onSectionsLoaded(courseId: String, sections: List<Section>) {
                progressBar.visibility = View.GONE

                val adapter = SectionAdapter(sections)
                adapter.setOnItemClickListener { _, _, position -> loadLessons(sections[position].id) }
                recyclerView.adapter = adapter
            }

            override fun onSectionsLoadFailed(courseId: String, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("CurriculumActivity", "Error loading", t)
                Toast.makeText(this@CurriculumActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun loadLessons(sectionId: String) {
        progressBar.visibility = View.VISIBLE
        CuriculumProvider.loadLessonsInSections(sectionId, object: CuriculumProvider.SectionLessonCallback {
            override fun onLessonsLoaded(sectionId: String, lessons: List<Lesson>) {
                progressBar.visibility = View.GONE

                val adapter = LessonAdapter(lessons)
                adapter.setOnItemClickListener { _, _, position -> loadVideos(lessons[position].id) }
                recyclerView.adapter = adapter
            }

            override fun onLessonsLoadFailed(sectionId: String, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("CurriculumActivity", "Error loading", t)
                Toast.makeText(this@CurriculumActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun loadVideos(lessonId: String) {
        progressBar.visibility = View.VISIBLE
        CuriculumProvider.loadVideosInLesson(lessonId, object: CuriculumProvider.LessonVideoCallback {

            override fun onVideosLoaded(lessonId: String, videos: List<Video>) {
                progressBar.visibility = View.GONE

                val adapter = VideoAdapter(videos)
                adapter.setOnItemClickListener { _, _, position ->
                    val intent = Intent(this@CurriculumActivity, MainActivity::class.java)
                    intent.putExtra(EXTRA_VIDEO_ID, videos[position].id)
                    this@CurriculumActivity.startActivity(intent)
                }
                recyclerView.adapter = adapter
            }

            override fun onVideosLoadFailed(lessonId: String, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("CurriculumActivity", "Error loading", t)
                Toast.makeText(this@CurriculumActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

}
