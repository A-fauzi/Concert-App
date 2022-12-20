package com.example.concert_app.apiConfig

import com.example.concert_app.service.api_whatsapp.WhatsappService
import com.example.concert_app.service.concert.ConcertService
import com.example.concert_app.service.user.UsersService
import com.example.concert_app.utils.WhatsappKeys
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkConfig(baseUrl: String) {
    val baseUrlInit = baseUrl

    // Set Interceptor
    fun getInterceptor(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val request = chain.request()
                val newRequest = request.newBuilder()
                    .header("Authorization", "Bearer " + WhatsappKeys.TEMPORARY_ACCESS_TOKEN)
                    .header("Content-Type", "application/json")
                    .build()
                chain.proceed(newRequest)
            })
            .addInterceptor(logging)
            .build()
    }

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrlInit)
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getUserService(): UsersService = getRetrofit().create(UsersService::class.java)
    fun getConcertService(): ConcertService = getRetrofit().create(ConcertService::class.java)
    fun whatsappService() : WhatsappService = getRetrofit().create(WhatsappService::class.java)
}