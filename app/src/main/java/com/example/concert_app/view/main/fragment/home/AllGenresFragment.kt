package com.example.concert_app.view.main.fragment.home

import android.content.Intent
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
import com.example.concert_app.remote.NetworkConfig
import com.example.concert_app.data.concert.ConcertResponse
import com.example.concert_app.databinding.FragmentAllGenresBinding
import com.example.concert_app.view.main.fragment.ConcertDetailActivity
import com.example.concert_app.view.main.fragment.home.adapter.AdapterListConcert
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AllGenresFragment : Fragment(), AdapterListConcert.CallClickListener {

    companion object {
        const val TAG = "AllGenresFragment"
    }

    private lateinit var binding: FragmentAllGenresBinding

    private lateinit var rvList: RecyclerView

    private lateinit var adapterListAllGenres: AdapterListConcert

    private lateinit var shimmerViewContainer: ShimmerFrameLayout

    private fun initView() {
        rvList = binding.rvAllConcert
        shimmerViewContainer = binding.shimmerViewContainerAllGenre
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

        shimmerViewContainer.startShimmer()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapterListAllGenres = AdapterListConcert(this@AllGenresFragment, arrayListOf())
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
                        rvList.visibility = View.VISIBLE
                        shimmerViewContainer.stopShimmer()
                        shimmerViewContainer.visibility = View.GONE

                    } else {
                        Log.d(TAG, "Response Not Successfully")
                    }
                }

                override fun onFailure(call: Call<ConcertResponse>, t: Throwable) {
                    Log.d(TAG, t.localizedMessage!!)
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

    override fun onClickListenerItem(data: ConcertResponse.Concert) {
        val bundle = Bundle()
        bundle.putString("photo_url", data.imgThumbnail)
        bundle.putString("title", data.title)
        bundle.putString("date_time", data.date + data.time)
        bundle.putString("location_name", data.locationName)
        bundle.putString("description", data.description)
        bundle.putString("description", data.description)
        bundle.putString("latitude", data.latitude)
        bundle.putString("longitude", data.longitude)

        val intent = Intent(requireActivity(), ConcertDetailActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

}