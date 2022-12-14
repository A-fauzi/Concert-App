package com.example.concert_app

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.example.concert_app.view.main.MainActivity
import com.squareup.picasso.Picasso
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

    fun getUserById(uid: String, TAG: String, photoUrl: ImageView, name: TextView, phone: TextView) {
        NetworkConfig()
            .getService()
            .getUserById(uid)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    Log.d(TAG, "DATA USER")
                    Log.d(TAG, "${response.body()?.data?.id}")

                    Picasso.get()
                        .load(response.body()?.data?.photoUrl)
                        .placeholder(R.drawable.profile_xample)
                        .error(R.mipmap.ic_launcher)
                        .into(photoUrl)

                    name.text = response.body()?.data?.name
                    phone.text = response.body()?.data?.phone

                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d(TAG, t.message.toString())
                }

            })
    }

    fun postData(name: String, phone: String, email:String, id: String) {
        val user = UserRequest(
            photoUrl = "https://ibb.co/XZ8hcVm",
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