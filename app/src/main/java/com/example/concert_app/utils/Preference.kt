package com.example.concert_app.utils

import android.content.Context
import android.content.SharedPreferences

object Preference {
    private lateinit var sharedPreferences: SharedPreferences
    fun saveData(context: Context, value: String, putStrKey: String = "PATH_STORAGE_KEY") {
        sharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("PATH_STORAGE_KEY", value)
        }.apply()
    }

    fun loadData(context: Context, getStrKey: String = "PATH_STORAGE_KEY"): String? {
        sharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        return sharedPreferences.getString("PATH_STORAGE_KEY", null)
    }
}