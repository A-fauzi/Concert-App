package com.example.concert_app.view.main.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.concert_app.data.user.UserModel
import com.example.concert_app.databinding.FragmentTicketBinding
import com.example.concert_app.utils.FirebaseServiceInstance
import com.example.concert_app.utils.FirebaseServiceInstance.databaseReference
import com.example.concert_app.view.main.fragment.adapter.AdapterListPerson
import com.example.concert_app.view.main.fragment.message.ChatActivity
import com.example.concert_app.view.main.fragment.message.MessageFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class TicketFragment : Fragment() {

    private lateinit var binding: FragmentTicketBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTicketBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}

