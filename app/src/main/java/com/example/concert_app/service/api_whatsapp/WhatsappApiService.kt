package com.example.concert_app.service.api_whatsapp

import android.util.Log
import com.example.concert_app.apiConfig.NetworkConfig
import com.example.concert_app.data.whatsapp.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WhatsappApiService {
    fun sendMessage(textVariable: String, languageId: String) {

        val parametersItem = ParametersItem(textVariable, "text")
        val arrPar = arrayListOf(parametersItem)

        val componentItem = ComponentsItem("body", arrPar)
        val arrComp = arrayListOf(componentItem)

        val language = Language(languageId)
        val template = Template(arrComp, "report_bug", language)
        val messageRequest = RequestWhatsappModelWithVariable(
            messagingProduct = "whatsapp",
            to = "6282112966360",
            type = "template",
            template = template,

        )
        NetworkConfig("https://graph.facebook.com/")
            .whatsappService()
            .sendMessageWa("109914878639779", messageRequest)
            .enqueue(object : Callback<WhatsappModel>{
                override fun onResponse(
                    call: Call<WhatsappModel>,
                    response: Response<WhatsappModel>
                ) {
                    Log.d("whatsapp", "response: $response")
                }

                override fun onFailure(call: Call<WhatsappModel>, t: Throwable) {
                    Log.d("whatsapp", "failure: ${t.message}")
                }


            })
    }
}