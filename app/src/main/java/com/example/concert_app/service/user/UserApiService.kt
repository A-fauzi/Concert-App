package com.example.concert_app.service.user

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.concert_app.remote.NetworkConfig
import com.example.concert_app.R
import com.example.concert_app.data.user.UserModel
import com.example.concert_app.data.user.UserRequest
import com.example.concert_app.data.user.UserResponse
import com.example.concert_app.view.main.MainActivity
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserApiService {
    fun getUsers() {
        NetworkConfig()
            .getUserService()
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

    fun getUserById(uid: String, TAG: String, photoUrl: ImageView, name: TextView, phone: TextView, progressBar: ProgressBar) {
        NetworkConfig()
            .getUserService()
            .getUserById(uid)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "DATA USER")
                        Log.d(TAG, "${response.body()?.data?.id}")

                        Picasso.get()
                            .load(response.body()?.data?.photoUrl)
                            .placeholder(R.drawable.profile_xample)
                            .error(R.mipmap.ic_launcher)
                            .into(photoUrl)

                        name.text = response.body()?.data?.name
                        phone.text = response.body()?.data?.phone

                        progressBar.visibility = View.GONE
                    } else {
                        Log.d(TAG, "Response Not Successfully")
                        progressBar.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d(TAG, t.message.toString())
                    progressBar.visibility = View.GONE
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
            .getUserService()
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

    fun updateUser(uid: String, name: String, phone: String, email:String, photoUrl: String) {
        val user = UserRequest(
            photoUrl = photoUrl,
            phone = phone,
            name = name,
            email = email,
            id = uid
        )
        NetworkConfig()
            .getUserService()
            .updateUsers(uid, user)
            .enqueue(object : Callback<UserResponse>{
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    Log.d(MainActivity.TAG, "Data Updated and add to API")
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d(MainActivity.TAG, t.localizedMessage!!)
                }

            })
    }
}