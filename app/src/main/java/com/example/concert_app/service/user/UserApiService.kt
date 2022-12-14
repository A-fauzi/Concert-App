package com.example.concert_app.service.user

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.concert_app.apiConfig.NetworkConfig
import com.example.concert_app.R
import com.example.concert_app.data.user.DataItem
import com.example.concert_app.data.user.UserModel
import com.example.concert_app.data.user.UserRequest
import com.example.concert_app.data.user.UserResponse
import com.example.concert_app.utils.Libs.dialogMessageAnimate
import com.example.concert_app.utils.Libs.simpleDateFormat
import com.example.concert_app.utils.LocalKeys
import com.example.concert_app.utils.Preference.saveData
import com.example.concert_app.view.main.MainActivity
import com.facebook.shimmer.ShimmerFrameLayout
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class UserApiService(context: Context) {

    private var ctx = context

    fun getUsers() {
        NetworkConfig(LocalKeys.LOCAL_BASE_URL)
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

    fun getUserById(
        uid: String,
        TAG: String,
        photoUrl: ImageView,
        name: TextView,
        email: TextView,
        phone: TextView,
        title: TextView,
        desc: TextView,
        shimmer: ShimmerFrameLayout,
        shimmerSatuView: ShimmerFrameLayout,
        profileLayout: LinearLayout,
        layoutItem1View: RelativeLayout,
    ) {
        NetworkConfig(LocalKeys.LOCAL_BASE_URL)
            .getUserService()
            .getUserById(uid)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.data?.pathStorageProfile?.let { saveData(ctx, it) }

                        shimmer.stopShimmer()
                        shimmer.visibility = View.GONE

                        shimmerSatuView.stopShimmer()
                        shimmerSatuView.visibility = View.GONE

                        profileLayout.visibility = View.VISIBLE
                        layoutItem1View.visibility = View.VISIBLE

                        when (response.body()?.data?.gender) {
                            "pria" -> {
                                Picasso.get()
                                    .load(response.body()?.data?.photoUrl)
                                    .placeholder(R.drawable.img_placeholder_man)
                                    .error(R.drawable.profile_xample)
                                    .into(photoUrl)
                            }
                            "wanita" -> {
                                Picasso.get()
                                    .load(response.body()?.data?.photoUrl)
                                    .placeholder(R.drawable.img_placeholder_woman)
                                    .error(R.drawable.img_error_woman)
                                    .into(photoUrl)
                            }
                        }

                        name.text = response.body()?.data?.name
                        phone.text = response.body()?.data?.phone
                        email.text = response.body()?.data?.email
                        title.text = response.body()?.data?.title
                        desc.text = response.body()?.data?.description


                    } else {
                        Log.d(TAG, "Response Not Successfully")
//                        WhatsappApiService().sendMessage("Response Not Successfully | UserApiService | Line 109", "en_US")
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    if (t is SocketTimeoutException) {
//                        WhatsappApiService().sendMessage("Response Not Successfully | UserApiService | Line 109", "en_US")
                        Log.d(TAG, t.message.toString())
                    } else {
//                        WhatsappApiService().sendMessage("Response Not Successfully | UserApiService | Line 109", "en_US")
                        Log.d(TAG, t.message.toString())
                    }
                }

            })
    }

    fun postData(name: String, phone: String, email: String, id: String, gender: String, layoutInflater: LayoutInflater, TAG: String) {
        val user = UserRequest(
            photoUrl = "https://ibb.co/XZ8hcVm",
            phone = phone,
            name = name,
            email = email,
            id = id,
            gender = gender,
            title = "Hobi | Pekerjaan | ETC",
            description = "Belum ingin menulis sesuatu",
            pathStorageProfile = "profile path"
        )
        NetworkConfig(LocalKeys.LOCAL_BASE_URL)
            .getUserService()
            .addUser(user)
            .enqueue(object : Callback<UserRequest> {
                override fun onResponse(call: Call<UserRequest>, response: Response<UserRequest>) {
                    Log.d(TAG, "Data created and add to API")
                }

                override fun onFailure(call: Call<UserRequest>, t: Throwable) {
                    if(t is SocketTimeoutException){
                        Toast.makeText(ctx, "Socket Time out. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                    dialogMessageAnimate(
                        layoutInflater,
                        ctx,
                        t.message.toString(),
                        R.raw.auth_failure,
                        "Failure"
                    )
                }

            })
    }

    fun updateUser(
        uid: String,
        name: String,
        phone: String,
        email: String,
        photoUrl: String,
        gender: String,
        title: String,
        description: String,
        pathStorageProfile: String,
        followers: List<DataItem>? = null,
        following: List<UserModel>? = null,
        date: String,
    ) {
        val user = UserRequest(
            photoUrl = photoUrl,
            phone = phone,
            name = name,
            email = email,
            id = uid,
            gender = gender,
            title = title,
            description = description,
            pathStorageProfile = pathStorageProfile,
            followers = followers,
            following = following,
            createdDate = date,
            modifiedDate = simpleDateFormat()
        )
        NetworkConfig(LocalKeys.LOCAL_BASE_URL)
            .getUserService()
            .updateUsers(uid, user)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    response.body()?.data?.pathStorageProfile?.let { saveData(ctx, it) }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d(MainActivity.TAG, t.localizedMessage!!)
                }

            })
    }


}