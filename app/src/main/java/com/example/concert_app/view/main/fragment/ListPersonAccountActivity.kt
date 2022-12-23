package com.example.concert_app.view.main.fragment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.concert_app.R
import com.example.concert_app.apiConfig.NetworkConfig
import com.example.concert_app.data.user.DataItem
import com.example.concert_app.data.user.UserModel
import com.example.concert_app.data.user.UserResponse
import com.example.concert_app.databinding.ActivityListPersonAccountBinding
import com.example.concert_app.service.user.UserApiService
import com.example.concert_app.utils.FirebaseServiceInstance
import com.example.concert_app.utils.FirebaseServiceInstance.auth
import com.example.concert_app.utils.FirebaseServiceInstance.user
import com.example.concert_app.utils.Libs
import com.example.concert_app.utils.Libs.simpleDateFormat
import com.example.concert_app.utils.LocalKeys
import com.example.concert_app.view.main.fragment.adapter.AdapterListPerson
import com.example.concert_app.view.main.fragment.message.ChatActivity
import com.example.concert_app.view.main.fragment.message.MessageFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class ListPersonAccountActivity : AppCompatActivity(), AdapterListPerson.CallClickListener {

    companion object {
        const val TAG = "ListPersonAccountActivity"
    }

    private lateinit var binding: ActivityListPersonAccountBinding

    private lateinit var listPerson: ArrayList<UserModel>
    private lateinit var rvList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListPersonAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getListPerson()
    }

    private fun getListPerson() {
        rvList = binding.rvListPerson
        rvList.setHasFixedSize(true)
        rvList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        listPerson = arrayListOf<UserModel>()

        FirebaseServiceInstance.databaseReference = FirebaseServiceInstance.firebaseDatabase.getReference("users")
        FirebaseServiceInstance.databaseReference.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    listPerson.clear()
                    for (list in snapshot.children) {
                        val lst = list.getValue(UserModel::class.java)
                        if (lst != null) {
                            listPerson.add(lst)
                        }
                    }
                    rvList.adapter = AdapterListPerson(this@ListPersonAccountActivity ,listPerson)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i(MessageFragment.TAG, error.message)
            }

        })

    }

    override fun onClickItemBtnMessage(data: UserModel) {
        val bundle = Bundle()
        bundle.putString("name", data.name)
        bundle.putString("photo", data.photoUrl)
        bundle.putString("userId", data.id)

        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onClickItemBtnFollow(data: UserModel) {
        val arr = arrayListOf<UserModel>()
        val arr1 = arrayListOf<DataItem>()
        Toast.makeText(this, " ${data.name} followed", Toast.LENGTH_SHORT).show()

        NetworkConfig(LocalKeys.LOCAL_BASE_URL)
            .getUserService()
            .getUserById(auth.currentUser?.uid ?: "")
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {

                        val id = response.body()?.data?.id
                        val name = response.body()?.data?.name
                        val phone = response.body()?.data?.phone
                        val email = response.body()?.data?.email
                        val photoUrl = response.body()?.data?.photoUrl
                        val gender = response.body()?.data?.gender
                        val title = response.body()?.data?.title
                        val desc = response.body()?.data?.description
                        val pathStorage = response.body()?.data?.pathStorageProfile
                        val date = response.body()?.data?.createdDate


                        arr.add(data)
                        response.body()?.data?.let { arr1.add(it) }

                        if (id != null && name != null && phone != null && email != null && photoUrl != null && gender != null && title != null && desc != null && pathStorage != null && date != null) {

                            UserApiService(this@ListPersonAccountActivity)
                                .updateUser(
                                    uid = id,
                                    name = name,
                                    phone = phone,
                                    email = email,
                                    photoUrl = photoUrl,
                                    gender = gender,
                                    title = title,
                                    following = arr,
                                    description = desc,
                                    pathStorageProfile = pathStorage,
                                    date = date
                                )


                            UserApiService(this@ListPersonAccountActivity)
                                .updateUser(
                                    uid = data.id.toString(),
                                    name = data.name.toString(),
                                    phone = data.phone.toString(),
                                    email = data.email.toString(),
                                    photoUrl = data.photoUrl.toString(),
                                    gender = data.gender.toString(),
                                    title = data.title.toString(),
                                    followers = arr1,
                                    description = data.description.toString(),
                                    pathStorageProfile = data.pathStorageProfile.toString(),
                                    date = data.createdDate.toString()
                                )
                        }


                    } else {
                        Log.d(TAG, "Response Not Successfully")
                        Libs.dialogMessageAnimate(
                            layoutInflater,
                            this@ListPersonAccountActivity,
                            "Data Not Updated",
                            R.raw.auth_failure,
                            "Failure"
                        )
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    if (t is SocketTimeoutException){
                        Log.d(TAG, "Error: ${t.message}")
                        Toast.makeText(this@ListPersonAccountActivity, "Error : ${t.message}", Toast.LENGTH_SHORT).show()
                        Libs.dialogMessageAnimate(
                            layoutInflater,
                            this@ListPersonAccountActivity,
                            "Error ${t.message}",
                            R.raw.auth_failure,
                            t.message.toString()
                        )
                    } else {
                        Log.d(TAG, "Error: ${t.message}")
                    }
                }

            })
    }
}