package com.example.concert_app.view.main.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.concert_app.ApiService
import com.example.concert_app.R
import com.example.concert_app.databinding.FragmentAccountBinding
import com.example.concert_app.utils.FirebaseServiceInstance.auth
import com.example.concert_app.utils.FirebaseServiceInstance.user
import com.example.concert_app.view.LoginActivity

class AccountFragment : Fragment() {

    companion object {
        const val TAG = "AccountFragment"
    }

    private lateinit var binding: FragmentAccountBinding

    private lateinit var photoUrl: ImageView
    private lateinit var name: TextView
    private lateinit var phone: TextView
    private lateinit var btnLogout: ImageView

    private fun initView() {
        photoUrl = binding.profileImage
        name = binding.profileName
        phone = binding.profilePhone
        btnLogout = binding.btnLogout
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(inflater, container, false)

        initView()

        val uid = user?.uid

        val apiService = ApiService()
        if (uid != null) {
            apiService.getUserById(uid, TAG, photoUrl, name, phone)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()
        }
    }
}