package com.example.concert_app.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import com.example.concert_app.ApiService
import com.example.concert_app.FirebaseServiceInstance.auth
import com.example.concert_app.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    companion object {
        const val TAG = "RegisterActivity"
    }

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var name: EditText
    private lateinit var phone: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText

    private fun initView() {
        name = binding.etName
        phone = binding.etPhone
        email = binding.etEmail
        password = binding.etPassword
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    override fun onStart() {
        super.onStart()

        binding.btnRegister.setOnClickListener {
            createUser()
        }

    }

    private fun createUser() {
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Log.d(TAG, "Success Create Email Password")

                    val uid = auth.currentUser!!.uid
                    val name = name.text.toString()
                    val phone = phone.text.toString()
                    val email = email.text.toString()
                    Log.d(TAG, uid)
                    Log.d(TAG, name)
                    Log.d(TAG, phone)
                    Log.d(TAG, email)

                    val apiService = ApiService()
                    apiService.postData(name, phone, email, uid)

                } else {
                    Log.d(TAG, "Failure Create Email Password")
                }
            }.addOnFailureListener {
                Log.d(TAG, it.message.toString())
            }
    }
}