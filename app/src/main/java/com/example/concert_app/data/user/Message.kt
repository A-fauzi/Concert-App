package com.example.concert_app.data.user

import com.google.gson.annotations.SerializedName

data class Message(
    val name: String? = null,

    val photoUrl: String? = null,

    val message: String? = null,

    val receiverId: String? = null,

    val time: String? = null
)
