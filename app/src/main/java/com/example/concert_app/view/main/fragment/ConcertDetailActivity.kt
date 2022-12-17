package com.example.concert_app.view.main.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.net.toUri
import com.example.concert_app.R
import com.example.concert_app.databinding.ActivityConcertDetailctivityBinding
import com.squareup.picasso.Picasso
import java.lang.Exception

class ConcertDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConcertDetailctivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConcertDetailctivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retrieveData()

    }

    private fun retrieveData() {
        val thumbnail = intent.extras?.getString("photo_url")
        val title = intent.extras?.getString("title")
        val dateTime = intent.extras?.getString("date_time")
        val locName = intent.extras?.getString("location_name")
        val desc = intent.extras?.getString("description")


        Picasso.get()
            .load(thumbnail)
            .placeholder(R.drawable.image_ini)
            .error(R.mipmap.ic_launcher)
            .into(binding.ivThumbnail)

        binding.tvTitle.text = title
        binding.tvDateTime.text = dateTime
        binding.tvLocationName.text = locName
        binding.tvDescription.text = desc
    }
}