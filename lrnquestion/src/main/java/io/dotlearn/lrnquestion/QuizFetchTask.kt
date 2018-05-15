package io.dotlearn.lrnquestion

import android.annotation.SuppressLint
import android.os.AsyncTask
import com.google.gson.Gson
import io.dotlearn.lrnquestion.utils.IoUtils
import io.dotlearn.lrnquestion.utils.Logger
import io.paperdb.Paper
import okhttp3.OkHttpClient
import okhttp3.Request

private const val KEY_QUIZ_DATA = "KEY_QUIZ_DATA_"

internal class QuizFetchTask {

    private val ioUtils = IoUtils()
    private val okHttpClient = OkHttpClient()
    private val gson = Gson()

    fun fetchQuiz(quizId: String, callback: QuizFetchCallback) {
        Logger.d("Fetching quiz for id: $quizId")
        callback.onQuizFetchStarted()
        val quizFromDb = getQuizFromDb(quizId)

        if(quizFromDb == null) {
            Logger.d("Quiz not in database, fetching from API")
            loadFromServer(quizId, callback)
        } else {
            Logger.d("Quiz in database, using")
            callback.onQuizFetched(quizFromDb)
        }
    }

    @SuppressLint("StaticFieldLeak")
    private fun loadFromServer(quizId: String, callback: QuizFetchCallback) {
        object: AsyncTask<Void, Void, QuizData?>() {

            override fun doInBackground(vararg p0: Void?): QuizData? {
                return getQuizFromApi(quizId)
            }

            override fun onPostExecute(quizFromApi: QuizData?) {
                if(!callback.isActive()) {
                    return
                }

                if(quizFromApi == null) {
                    callback.onQuizFetchError()
                } else {
                    saveQuizInDb(quizId, quizFromApi)
                    callback.onQuizFetched(quizFromApi)
                }
            }

        }.execute()
    }

    private fun getQuizFromApi(quizId: String): QuizData? {
        val url = "https://api.dotlearn.io/curriculum/question/$quizId"
        val request = Request.Builder()
                .url(url)
                .build()

        val response = okHttpClient.newCall(request).execute()
        return if(response.isSuccessful) {
            val responseString = response.body()?.string()
            Logger.d("Fetched quiz from api: $responseString")

            if(responseString != null) {
                gson.fromJson<QuizData>(responseString, QuizData::class.java)
            } else {
                Logger.e("Quiz response string is null")
                null
            }
        } else {
            Logger.e("Failed to load quiz: + $response")
            null
        }
    }

    private fun getQuizFromDb(quizId: String): QuizData? {
        /*return Quiz(1, "Explanation",
                Quiz.Hint(null, null)
                , "Who is the president of Nigeria?",
                listOf(
                        Quiz.Option("Muhammed Buhari", "E"),
                        Quiz.Option("Goodluck Jonathan", "E"),
                        Quiz.Option("Olusegun Obasanjo", "E"),
                        Quiz.Option("Soft work", "E")
                ), null)*/
        return Paper.book().read(getDbKey(quizId), null)
    }

    private fun saveQuizInDb(quizId: String, quiz: QuizData) {
        Paper.book().write(getDbKey(quizId), quiz)
    }

    private fun getDbKey(quizId: String) = ioUtils.md5(KEY_QUIZ_DATA + quizId)

    interface QuizFetchCallback {

        fun onQuizFetchStarted()
        fun onQuizFetched(quiz: QuizData)
        fun onQuizFetchError()
        fun isActive(): Boolean

    }

}