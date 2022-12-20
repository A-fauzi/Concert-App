package com.example.concert_app.apiConfig

import okhttp3.Interceptor
import okhttp3.Response

class MyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
            .header("Authorization", "Bearer EAAL0OLohei4BALuKTUKcUJFeNNr2HFfDdxEc6RMLfTCpn7jsnMhRX13M6c6I8spgSdFJce9LnXrBP0cauBC2GbJ8ZCKO8PW6zrUABfjMqOVfMOT9kIRcA2ZC5z3oi6nZAPHUiWSmDB5OanknVBA6YjcZABJAMDCimH5lVZC84ExxgJczHyA9GqD674sdB2ggZBY6CZBhU4hTPtMbhYP6XNR")
            .header("Content-Type", "application/json")
            .build()
        return chain.proceed(newRequest)
    }
}