package com.example.concert_app.data.user

import com.google.gson.annotations.SerializedName

data class Message(
    val name: String? = null,

    @SerializedName("photo_url")
    val photo_url: String? = null,

    @SerializedName("text_message")
    val text_message: String? = null,

    @SerializedName("time_message")
    val time_message: String? = null
)
