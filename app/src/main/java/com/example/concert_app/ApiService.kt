package com.example.concert_app

import android.util.Log
import com.example.concert_app.view.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiService {
    fun getUsers() {
        NetworkConfig()
            .getService()
            .getUsers()
            .enqueue(object : Callback<List<UserModel>> {
                override fun onResponse(
                    call: Call<List<UserModel>>,
                    response: Response<List<UserModel>>
                ) {
                    Log.d(MainActivity.TAG, response.message())
                }

                override fun onFailure(call: Call<List<UserModel>>, t: Throwable) {
                    Log.d(MainActivity.TAG, t.localizedMessage!!)
                }

            })
    }

    fun postData(name: String, phone: String, email:String, id: String) {
        val user = UserRequest(
            photoUrl = "https://www.picture.com",
            phone = phone,
            name = name,
            email = email,
            id = id
        )
        NetworkConfig()
            .getService()
            .addUser(user)
            .enqueue(object: Callback<UserRequest> {
                override fun onResponse(call: Call<UserRequest>, response: Response<UserRequest>) {
                    Log.d(MainActivity.TAG, "Data created and add to API")
                }

                override fun onFailure(call: Call<UserRequest>, t: Throwable) {
                    Log.d(MainActivity.TAG, t.message.toString())
                }

            })
    }
}