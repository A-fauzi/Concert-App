package com.example.concert_app.view.main.fragment.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.concert_app.R

class ChatActivity : AppCompatActivity() {

    private lateinit var textName: TextView

    private fun initView(){
        textName = findViewById(R.id.tv_user_name_receiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        initView()

        textName.text =  intent.extras?.getString("name")
    }
}