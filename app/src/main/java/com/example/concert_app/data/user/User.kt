package com.example.concert_app.data.user

import com.google.gson.annotations.SerializedName

data class User(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataItem(

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
	val email: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	val followers: List<UserModel>? = null,
	val following: List<UserModel>? = null,

	@field:SerializedName("pathStorageProfile")
	val pathStorageProfile: String? = null
)
