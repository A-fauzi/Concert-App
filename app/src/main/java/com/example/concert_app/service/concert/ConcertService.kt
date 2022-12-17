package com.example.concert_app.service.concert

import com.example.concert_app.data.concert.ConcertResponse
import com.example.concert_app.data.user.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ConcertService {
    @GET("concerts")
    fun getConcerts(): Call<ConcertResponse>

    @GET("concerts/genre")
    fun getConcertByGenre(@Query("genre") genre: String): Call<ConcertResponse>

    @GET("concerts/artist")
    fun getConcertByArtist(@Query("name") artistName: String): Call<ConcertResponse>
}