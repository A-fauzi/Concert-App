package com.example.concert_app.data.whatsapp

import android.util.Log
import com.example.concert_app.apiConfig.NetworkConfig
import com.example.concert_app.utils.WhatsappKeys
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class ServiceImplement {
    fun sendMessage(templateName: String, paramText1: String, paramText2: String, paramText3: String, langCode: String) {

        val language = Language(langCode)

        val parameter1 = ParametersItem(paramText1, "text")
        val parameter2 = ParametersItem(paramText2, "text")
        val parameter3 = ParametersItem(paramText3, "text")
        val arrPar = arrayListOf(parameter1, parameter2, parameter3)

        val componentItem = ComponentsItem(
            "body",
            arrPar
        )
        val arrComp = arrayListOf(componentItem)

        val template = Template(
            components = arrComp,
            name = templateName,
            language = language
        )
        val request = WhatsappMessageRequest(
            template = template,
            messagingProduct = "whatsapp",
            to = "6282112966360",
            type = "template"
        )


        NetworkConfig(WhatsappKeys.BASE_URL)
            .whatsappService()
            .sendMessage(request)
            .enqueue(object : Callback<WhatsappMessageRequest>{
                override fun onResponse(
                    call: Call<WhatsappMessageRequest>,
                    response: Response<WhatsappMessageRequest>
                ) {
                    Log.d("whatsapp", "Pesan terkirim")
                }

                override fun onFailure(call: Call<WhatsappMessageRequest>, t: Throwable) {
                    if (t is SocketTimeoutException) {
                        Log.d("whatsapp", "${t.message}")
                    } else {
                        Log.d("whatsapp", "${t.message}")
                    }
                }

            })
    }
}