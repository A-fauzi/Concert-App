package com.example.concert_app.data.concert

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.auth.User
import com.google.gson.annotations.SerializedName

data class ConcertResponse(

	@field:SerializedName("data")
	val data: ArrayList<Concert>? = null,

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
) {
	@Entity
	data class Concert(
		@field:SerializedName("imgThumbnail")
		@ColumnInfo(name = "img_thumbnail")
		val imgThumbnail: String? = null,

		@field:SerializedName("date")
		@ColumnInfo(name = "date")
		val date: String? = null,

		@field:SerializedName("locationName")
		@ColumnInfo(name = "location_name")
		val locationName: String? = null,

		@field:SerializedName("genreMusic")
		@ColumnInfo(name = "genre_music")
		val genreMusic: String? = null,

		@field:SerializedName("createdDate")
		@ColumnInfo(name = "created_date")
		val createdDate: String? = null,

		@field:SerializedName("artist")
		@ColumnInfo(name = "artist")
		val artist: String? = null,

		@field:SerializedName("modifiedDate")
		@ColumnInfo(name = "modified_date")
		val modifiedDate: String? = null,

		@field:SerializedName("description")
		@ColumnInfo(name = "description")
		val description: String? = null,

		@field:SerializedName("locationCoordinate")
		@ColumnInfo(name = "location_coordinate")
		val locationCoordinate: String? = null,

		@field:SerializedName("id")
		@PrimaryKey
		val id: String? = null,

		@field:SerializedName("time")
		@ColumnInfo(name = "time")
		val time: String? = null,

		@field:SerializedName("title")
		@ColumnInfo(name = "title")
		val title: String? = null,

		@field:SerializedName("latitude")
		@ColumnInfo(name = "latitude")
		val latitude: String? = null,

		@field:SerializedName("longitude")
		@ColumnInfo(name = "longitude")
		val longitude: String? = null,
	)
}
