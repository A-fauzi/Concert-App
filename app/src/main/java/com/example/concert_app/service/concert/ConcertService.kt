package com.example.concert_app.service.concert

import com.example.concert_app.data.concert.ConcertResponse
import com.example.concert_app.data.user.UserResponse
import retrofit2.Call
import retrofit2.http.GET

interface ConcertService {
    @GET("concerts")
    fun getConcerts(): Call<ConcertResponse>
}