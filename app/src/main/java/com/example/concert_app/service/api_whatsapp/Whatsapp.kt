package com.example.concert_app.service.api_whatsapp

import com.example.concert_app.data.whatsapp.RequestWhatsappModelWithVariable
import com.example.concert_app.data.whatsapp.WhatsappModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface Whatsapp {
    @POST("{phoneNoId}/messages")
    fun sendMessageWa(@Path("phoneNoId") phoneNoId: String, @Body sendMessage: RequestWhatsappModelWithVariable) : Call<WhatsappModel>

}