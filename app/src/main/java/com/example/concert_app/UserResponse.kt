package com.example.concert_app

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @field:SerializedName("data")
    val data: DataItem? = null,

    @field:SerializedName("message")
    val message: String? = null
)
