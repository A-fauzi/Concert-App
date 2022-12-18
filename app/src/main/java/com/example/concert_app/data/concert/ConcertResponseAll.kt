package com.example.concert_app.data.concert

import com.google.gson.annotations.SerializedName

data class ConcertResponseAll(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataItem(

	@field:SerializedName("imgThumbnail")
	val imgThumbnail: String? = null,

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("locationName")
	val locationName: String? = null,

	@field:SerializedName("genreMusic")
	val genreMusic: String? = null,

	@field:SerializedName("createdDate")
	val createdDate: String? = null,

	@field:SerializedName("artist")
	val artist: String? = null,

	@field:SerializedName("modifiedDate")
	val modifiedDate: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("locationCoordinate")
	val locationCoordinate: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null,
)
