package com.example.concert_app.view.main.fragment.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.concert_app.remote.NetworkConfig
import com.example.concert_app.data.concert.ConcertResponse
import com.example.concert_app.databinding.FragmentAllGenresBinding
import com.example.concert_app.view.main.fragment.home.adapter.AdapterListAllGenres
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AllGenresFragment : Fragment() {

    companion object {
        const val TAG = "AllGenresFragment"
    }

    private lateinit var binding: FragmentAllGenresBinding

    private lateinit var rvList: RecyclerView

    private lateinit var adapterListAllGenres: AdapterListAllGenres

    private lateinit var progressbar: ProgressBar

    private fun initView() {
        rvList = binding.rvAllConcert
        progressbar = binding.progressbar
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAllGenresBinding.inflate(inflater, container, false)

        initView()

        setupRecyclerView()

        getDataConcert()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapterListAllGenres = AdapterListAllGenres(arrayListOf())
        rvList.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = adapterListAllGenres
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getDataConcert() {
        NetworkConfig()
            .getConcertService()
            .getConcerts()
            .enqueue(object : Callback<ConcertResponse> {
                override fun onResponse(
                    call: Call<ConcertResponse>,
                    response: Response<ConcertResponse>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        showDataConcert(result!!)
                        progressbar.visibility = View.GONE
                        rvList.visibility = View.VISIBLE

                    } else {
                        progressbar.visibility = View.GONE
                        Log.d(TAG, "Response Not Successfully")
                    }
                }

                override fun onFailure(call: Call<ConcertResponse>, t: Throwable) {
                    Log.d(TAG, t.localizedMessage!!)
                    progressbar.visibility = View.GONE
                }

            })
    }

    private fun showDataConcert(concert: ConcertResponse) {
        val result = concert.data
        if (result != null) {
            adapterListAllGenres.setData(result)
        }
//        for (res in concert) {
//            Log.d(TAG, "artist: ${res.data?.title}")
//        }
    }

}