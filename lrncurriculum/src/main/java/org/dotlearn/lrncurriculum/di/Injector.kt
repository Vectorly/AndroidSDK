package org.dotlearn.lrncurriculum.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.dotlearn.lrncurriculum.BASE_URL
import org.dotlearn.lrncurriculum.BuildConfig
import org.dotlearn.lrncurriculum.CurriculumService
import org.dotlearn.lrncurriculum.data.local.*
import org.dotlearn.lrncurriculum.data.remote.*
import org.dotlearn.lrncurriculum.utils.IoUtils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object Injector {

    private lateinit var curriculumService: CurriculumService

    private fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(provideHttpLoggingInterceptor())
                .build()
    }

    private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
        else {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        return httpLoggingInterceptor
    }

    private fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private fun provideCurriculumService(): CurriculumService {
        if(!::curriculumService.isInitialized) {
            curriculumService = provideRetrofit().create(CurriculumService::class.java)
        }

        return curriculumService
    }

    internal fun provideCourseDb(): CourseDb {
        return CourseDb()
    }

    internal fun provideCourseLoader(): CourseLoader {
        return CourseLoader(provideCurriculumService())
    }

    internal fun provideSectionDb(): SectionDb {
        return SectionDb()
    }

    internal fun provideSectionLoader(): SectionLoader {
        return SectionLoader(provideCurriculumService())
    }

    internal fun provideLessonDb(): LessonDb {
        return LessonDb()
    }

    internal fun provideLessonLoader(): LessonLoader {
        return LessonLoader(provideCurriculumService())
    }

    internal fun provideVideoDb(): VideoDb {
        return VideoDb()
    }

    internal fun provideQuizDb(): QuizDb {
        return QuizDb()
    }

    internal fun provideVideoLoader(): VideoLoader {
        return VideoLoader(provideCurriculumService())
    }

    internal fun provideSearchLoader(): SearchLoader {
        return SearchLoader(provideCurriculumService())
    }

    internal fun provideQuizLoader(): QuizLoader {
        return QuizLoader(provideCurriculumService())
    }

    internal fun provideIoUtils() = IoUtils()

}