package com.example.concert_app.data.user

import com.google.gson.annotations.SerializedName

data class UserRequest(
    @field:SerializedName("photoUrl")
    val photoUrl: String? = null,

    @field:SerializedName("phone")
    val phone: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("gender")
    val gender: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("pathStorageProfile")
    val pathStorageProfile: String? = null
)