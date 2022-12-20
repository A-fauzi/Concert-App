package com.example.concert_app.service.api_whatsapp

import com.example.concert_app.data.whatsapp.WhatsappMessageRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface WhatsappService {
    @POST("v15.0/109914878639779/messages")
    fun sendMessage(@Body sendRequest: WhatsappMessageRequest) : Call<WhatsappMessageRequest>
}