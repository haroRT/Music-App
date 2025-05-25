package com.example.haonv.data.remote

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val MUSIC_BASE_URL = "https://static.apero.vn"
    private const val RANK_BASE_URL = "https://ws.audioscrobbler.com"
    private const val REQUEST_TIMEOUT = 30L
    private  val musicRetrofit by lazy { createMusicRetrofit() }
    private val rankRetrofit by lazy { createRankRetrofit() }
    val musicService by lazy { musicRetrofit.create(MusicApi::class.java) }
    val rankService by lazy { rankRetrofit.create(RankApi::class.java) }

    private fun createMusicRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MUSIC_BASE_URL)
            .client(buildClient())
            .addConverterFactory(GsonConverterFactory.create(gsonConfig))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    private fun createRankRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(RANK_BASE_URL)
            .client(buildClient())
            .addConverterFactory(GsonConverterFactory.create(gsonConfig))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    private fun buildClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    private var gsonConfig = GsonBuilder().create()
}