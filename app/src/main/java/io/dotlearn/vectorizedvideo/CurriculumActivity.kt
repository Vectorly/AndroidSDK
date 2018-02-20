package io.dotlearn.vectorizedvideo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_curriculum.*
import org.dotlearn.lrncurriculum.CurriculumProvider
import org.dotlearn.lrncurriculum.models.Course
import org.dotlearn.lrncurriculum.models.Lesson
import org.dotlearn.lrncurriculum.models.Section
import org.dotlearn.lrncurriculum.models.Video

@SuppressLint("StaticFieldLeak")
class CurriculumActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_curriculum)

        CurriculumProvider.init(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        loadCourses()

        object: AsyncTask<Void, Void, Boolean>() {

            override fun doInBackground(vararg p0: Void?): Boolean {
                try {
                    //CurriculumSync.syncAll()
                }
                catch (e: Exception) {
                    e.printStackTrace()
                }
                return true
            }

        }.execute()
    }

    private fun loadCourses() {
        progressBar.visibility = View.VISIBLE
        object: AsyncTask<Void, Void, List<Course>>() {

            override fun doInBackground(vararg params: Void?): List<Course>? {
                return try {
                    CurriculumProvider.getCourses()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("boom", e.toString())
                    null
                }
            }

            override fun onPostExecute(result: List<Course>?) {
                if(result != null) {
                    progressBar.visibility = View.GONE

                    val adapter = CourseAdapter(result)
                    adapter.setOnItemClickListener { _, _, position -> loadSections(result[position].id) }
                    recyclerView.adapter = adapter
                }
                else {
                    progressBar.visibility = View.GONE
                    Log.e("CurriculumActivity", "Error loading")
                    Toast.makeText(this@CurriculumActivity, "Error loading courses", Toast.LENGTH_LONG).show()
                }
            }

        }.execute()
    }

    private fun loadSections(courseId: String) {
        progressBar.visibility = View.VISIBLE
        object: AsyncTask<Void, Void, List<Section>>() {

            override fun doInBackground(vararg params: Void?): List<Section>? {
                return try {
                    CurriculumProvider.getSections(courseId)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("boom", e.toString())
                    null
                }
            }

            override fun onPostExecute(result: List<Section>?) {
                if(result != null) {
                    progressBar.visibility = View.GONE

                    val adapter = SectionAdapter(result)
                    adapter.setOnItemClickListener { _, _, position -> loadLessons(result[position].id) }
                    recyclerView.adapter = adapter
                }
                else {
                    progressBar.visibility = View.GONE
                    Log.e("CurriculumActivity", "Error loading")
                    Toast.makeText(this@CurriculumActivity, "Error loading sections", Toast.LENGTH_LONG).show()
                }
            }

        }.execute()
    }

    private fun loadLessons(sectionId: String) {
        progressBar.visibility = View.VISIBLE
        object: AsyncTask<Void, Void, List<Lesson>>() {

            override fun doInBackground(vararg params: Void?): List<Lesson>? {
                return try {
                    CurriculumProvider.getLessons(sectionId)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("boom", e.toString())
                    null
                }
            }

            override fun onPostExecute(result: List<Lesson>?) {
                if(result != null) {
                    progressBar.visibility = View.GONE

                    val adapter = LessonAdapter(result)
                    adapter.setOnItemClickListener { _, _, position -> loadVideos(result[position].id) }
                    recyclerView.adapter = adapter
                }
                else {
                    progressBar.visibility = View.GONE
                    Log.e("CurriculumActivity", "Error loading")
                    Toast.makeText(this@CurriculumActivity, "Error loading lessons", Toast.LENGTH_LONG).show()
                }
            }

        }.execute()
    }

    private fun loadVideos(lessonId: String) {
        progressBar.visibility = View.VISIBLE
        object: AsyncTask<Void, Void, List<Video>>() {

            override fun doInBackground(vararg params: Void?): List<Video>? {
                return try {
                    CurriculumProvider.getVideos(lessonId)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("boom", e.toString())
                    null
                }
            }

            override fun onPostExecute(result: List<Video>?) {
                if(result != null) {
                    progressBar.visibility = View.GONE

                    val adapter = VideoAdapter(result)
                    adapter.setOnItemClickListener { _, _, position ->
                        val intent = Intent(this@CurriculumActivity, MainActivity::class.java)
                        intent.putExtra(EXTRA_VIDEO_ID, result[position].id)
                        this@CurriculumActivity.startActivity(intent)
                    }
                    recyclerView.adapter = adapter
                }
                else {
                    progressBar.visibility = View.GONE
                    Log.e("CurriculumActivity", "Error loading")
                    Toast.makeText(this@CurriculumActivity, "Error loading videos", Toast.LENGTH_LONG).show()
                }
            }

        }.execute()
    }

}
