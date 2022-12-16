package com.example.concert_app.view.main.fragment.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.concert_app.R
import com.example.concert_app.data.concert.ConcertResponse
import com.example.concert_app.databinding.FragmentSearchConcertBinding
import com.example.concert_app.remote.NetworkConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchConcertFragment : Fragment() {

    private lateinit var binding: FragmentSearchConcertBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchConcertBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSearch.setOnClickListener {
            val inputSearch = binding.etSearchConcert.text

        }
    }


}