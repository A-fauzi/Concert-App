package com.example.concert_app

import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.*

interface UsersService {
    @GET("users")
    fun getUsers(): Call<List<UserModel>>

    @POST("users")
    fun addUser(@Body userData: UserRequest): Call<UserRequest>

    @GET("users/{id}")
    fun getUserById(@Path("id") userId: String): Call<UserResponse>

}