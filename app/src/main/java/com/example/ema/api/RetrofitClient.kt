package com.example.ema.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://45.149.76.155:8080/api/"

    val httpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS) // Set connection timeout
        .readTimeout(30, TimeUnit.SECONDS) // Set read timeout
        .writeTimeout(30, TimeUnit.SECONDS) // Set write timeout
        .build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }
}
