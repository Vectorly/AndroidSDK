package io.vectorly.player.loader.di

import io.vectorly.player.BuildConfig
import io.vectorly.player.loader.FileUtils
import io.vectorly.player.loader.download.Downloader
import io.vectorly.player.loader.videodata.BASE_URL
import io.vectorly.player.loader.videodata.VideoDataFetcher
import io.vectorly.player.loader.videodata.VideoService
import io.vectorly.player.utils.IoUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Dependency injection helper object
 */

internal object Injector {

    private lateinit var videoService: VideoService
    private lateinit var okHttpClient: OkHttpClient

    private fun provideOkHttpClient(): OkHttpClient {
        if(!::okHttpClient.isInitialized) {
            okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(provideHttpLoggingInterceptor())
                    .build()
        }

        return okHttpClient
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

    private fun provideVideoService(): VideoService {
        if(!::videoService.isInitialized) {
            videoService = provideRetrofit().create(VideoService::class.java)
        }

        return videoService
    }

    internal fun provideVideoDataFetcher() = VideoDataFetcher(provideVideoService())

    internal fun provideIoUtils() = IoUtils()

    internal fun provideDownloader() = Downloader(provideOkHttpClient(), provideIoUtils())

    internal fun provideFileUtils() = FileUtils()

}