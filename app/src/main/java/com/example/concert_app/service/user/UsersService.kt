package com.example.concert_app.service.user

import com.example.concert_app.data.user.UserModel
import com.example.concert_app.data.user.UserRequest
import com.example.concert_app.data.user.UserResponse
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