package com.example.concert_app

import com.google.gson.annotations.SerializedName

data class UserModel(
    @field:SerializedName("photoUrl")
    val photoUrl: String? = null,

    @field:SerializedName("createdDate")
    val createdDate: String? = null,

    @field:SerializedName("phone")
    val phone: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("modifiedDate")
    val modifiedDate: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("email")
    val email: String? = null
)
