package com.example.concert_app

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface UsersService {
    @GET("users")
    fun getUsers(): Call<List<UserModel>>

    @POST("users")
    fun addUser(@Body userData: UserRequest): Call<UserRequest>
}