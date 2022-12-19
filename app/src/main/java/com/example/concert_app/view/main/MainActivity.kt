package com.example.concert_app.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.concert_app.*
import com.example.concert_app.databinding.ActivityMainBinding
import com.example.concert_app.view.main.fragment.AccountFragment
import com.example.concert_app.view.main.fragment.home.HomeFragment
import com.example.concert_app.view.main.fragment.MessageFragment
import com.example.concert_app.view.main.fragment.TicketFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.OkHttpClient

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(HomeFragment())
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.ticket -> {
                    loadFragment(TicketFragment())
                    true
                }
                R.id.msg -> {
                    loadFragment(MessageFragment())
                    true
                }
                R.id.account -> {
                    loadFragment(AccountFragment())
                    true
                }
                else -> {
                    false
                }
            }
        }

    }


    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }



}