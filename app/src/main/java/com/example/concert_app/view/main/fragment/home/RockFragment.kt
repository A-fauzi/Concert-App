package com.example.concert_app.view.main.fragment.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.concert_app.data.concert.ConcertResponse
import com.example.concert_app.databinding.FragmentRockBinding
import com.example.concert_app.remote.NetworkConfig
import com.example.concert_app.view.main.fragment.home.adapter.AdapterListConcert
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RockFragment : Fragment(), AdapterListConcert.CallClickListener {

    private lateinit var binding: FragmentRockBinding

    private lateinit var rvList: RecyclerView

    private lateinit var adapterList: AdapterListConcert

    private lateinit var progressbar: ProgressBar

    private fun initView() {
        rvList = binding.rvIndieConcert
        progressbar = binding.progressbar
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRockBinding.inflate(inflater, container, false)

        initView()

        setupRecyclerView()

        getDataConcertByGenre("ROCK")

        return binding.root
    }

    private fun setupRecyclerView() {
        adapterList = AdapterListConcert(this@RockFragment, arrayListOf())
        rvList.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = adapterList
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getDataConcertByGenre(genre: String) {
        NetworkConfig()
            .getConcertService()
            .getConcertByGenre(genre)
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
                        Log.d(AllGenresFragment.TAG, "Response Not Successfully")
                    }
                }

                override fun onFailure(call: Call<ConcertResponse>, t: Throwable) {
                    Log.d(AllGenresFragment.TAG, t.localizedMessage!!)
                    progressbar.visibility = View.GONE
                }

            })
    }

    private fun showDataConcert(concert: ConcertResponse) {
        val result = concert.data
        if (result != null) {
            adapterList.setData(result)
        }
//        for (res in concert) {
//            Log.d(TAG, "artist: ${res.data?.title}")
//        }
    }

    override fun onClickListenerItem(data: ConcertResponse.Concert) {
        Toast.makeText(requireActivity(), data.artist, Toast.LENGTH_SHORT).show()
    }

}