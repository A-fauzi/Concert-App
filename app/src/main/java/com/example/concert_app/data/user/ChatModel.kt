package com.example.concert_app.data.user

data class ChatModel(
    var senderId: String? = null,
    var receiverId: String? = null,
    var message: String? = null,
    var time: String? = null,
    var photoUrl: String? = null
)
