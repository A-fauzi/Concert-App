package com.example.concert_app.remote

import com.example.concert_app.service.concert.ConcertService
import com.example.concert_app.service.user.UsersService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkConfig {
    // Set Interceptor
    fun getInterceptor(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.43.88:8080")
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getUserService(): UsersService = getRetrofit().create(UsersService::class.java)
    fun getConcertService(): ConcertService = getRetrofit().create(ConcertService::class.java)
}