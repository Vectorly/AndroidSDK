package com.mobymagic.vectorizedvideo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import io.dotlearn.lrnquestion.QuizData
import io.dotlearn.lrnquestion.QuizFragment
import io.dotlearn.vectorizedvideo.R
import kotlinx.android.synthetic.main.activity_curriculum.*
import org.dotlearn.lrncurriculum.CurriculumProvider
import org.dotlearn.lrncurriculum.models.*

@SuppressLint("StaticFieldLeak")
class CurriculumActivity : AppCompatActivity(), QuizFragment.OnFragmentInteractionListener {

    private var lastSelectedCourseId: String? = null
    private var lastSelectedSectionId: String? = null
    private var lastSelectedLessonId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_curriculum)

        CurriculumProvider.init(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        loadCourses()

        requestPermission()
    }

    override fun onOptionSelected(quiz: QuizData?, userSelectedOption: Int, quizFragment: QuizFragment) {
        quizFragment.showAnswerAndExplanation()
    }

    override fun onQuizDataLoaded(quizData: QuizData?, quizFragment: QuizFragment) {
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    101)
        } else {
            // Permission has already been granted
        }
    }

    private fun loadCourses() {
        canQuit = true
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
                    adapter.setOnItemClickListener { _, _, position ->
                        canQuit = false
                        lastSelectedCourseId = result[position].id
                        loadSections(result[position].id)
                    }
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
                    adapter.setOnItemClickListener { _, _, position ->
                        canQuit = false
                        lastSelectedSectionId = result[position].id
                        loadLessons(result[position].id)
                    }
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
                    adapter.setOnItemClickListener { _, _, position ->
                        canQuit = false
                        lastSelectedLessonId = result[position].id
                        loadQuizzes(result[position].id)
                    }
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

    private fun loadQuizzes(lessonId: String) {
        progressBar.visibility = View.VISIBLE
        object: AsyncTask<Void, Void, List<Quiz>>() {

            override fun doInBackground(vararg params: Void?): List<Quiz>? {
                return try {
                    CurriculumProvider.getQuizzes(lessonId)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("boom", e.toString())
                    null
                }
            }

            override fun onPostExecute(result: List<Quiz>?) {
                if(result != null) {
                    progressBar.visibility = View.GONE

                    val adapter = QuizAdapter(result)
                    adapter.setOnItemClickListener { _, _, position ->
                        findViewById<View>(R.id.questions_container)?.visibility = View.VISIBLE
                        supportFragmentManager.beginTransaction().replace(R.id.questions_container,
                                QuizFragment.newInstance(result[position].id)).commit()
                    }
                    recyclerView.adapter = adapter
                }
                else {
                    progressBar.visibility = View.GONE
                    Log.e("CurriculumActivity", "Error loading")
                    Toast.makeText(this@CurriculumActivity, "Error loading quizzes", Toast.LENGTH_LONG).show()
                }
            }

        }.execute()
    }

    private var canQuit = true

    override fun onBackPressed() {
        findViewById<View>(R.id.questions_container)?.visibility = View.GONE

        when {
            lastSelectedSectionId != null -> {
                loadLessons(lastSelectedSectionId!!)
                lastSelectedSectionId = null
            }
            lastSelectedCourseId != null -> {
                loadSections(lastSelectedCourseId!!)
                lastSelectedCourseId = null
            }
            canQuit -> finish()
            else -> loadCourses()
        }
    }

}
