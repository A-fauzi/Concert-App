package com.example.concert_app.view.main.fragment.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.concert_app.R
import com.example.concert_app.data.concert.ConcertResponse
import com.example.concert_app.databinding.FragmentSearchConcertBinding
import com.example.concert_app.apiConfig.NetworkConfig
import com.example.concert_app.utils.Libs.dialogMessageAnimate
import com.example.concert_app.utils.LocalKeys.LOCAL_BASE_URL
import com.example.concert_app.view.main.fragment.home.adapter.AdapterListConcert
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException


class SearchConcertFragment : Fragment(), AdapterListConcert.CallClickListener {

    private lateinit var binding: FragmentSearchConcertBinding

    private lateinit var searchInput: EditText

    private lateinit var rvList: RecyclerView

    private lateinit var adapterList: AdapterListConcert


    private fun initView() {
        searchInput = binding.etSearchConcert
        rvList = binding.rvListConcertSearch
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchConcertBinding.inflate(inflater, container, false)

        initView()

        setupRecyclerView()

        getDataConcertByArtist("")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //========================================== |TextWatcher| ==================================================================
        searchInput.addTextChangedListener(GenericTextWatcher(searchInput))
        //========================================== |END| ==================================================================

    }

    private fun setupRecyclerView() {
        adapterList = AdapterListConcert(this@SearchConcertFragment, arrayListOf())
        rvList.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = adapterList
        }
    }

    private fun getDataConcertByArtist(artistName: String) {
        NetworkConfig(LOCAL_BASE_URL)
            .getConcertService()
            .getConcertByArtist(artistName)
            .enqueue(object : Callback<ConcertResponse> {
                override fun onResponse(
                    call: Call<ConcertResponse>,
                    response: Response<ConcertResponse>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        showDataConcert(result!!)
                        rvList.visibility = View.VISIBLE

                    } else {
                        Log.d(AllGenresFragment.TAG, "Response Not Successfully")
                    }
                }

                override fun onFailure(call: Call<ConcertResponse>, t: Throwable) {
                    if(t is SocketTimeoutException){
                        Toast.makeText(requireActivity(), "Socket Time out. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                    dialogMessageAnimate(
                        layoutInflater,
                        requireContext(),
                        t.message.toString(),
                        R.raw.auth_failure,
                        "Failure"
                    )
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

    inner class GenericTextWatcher(private val view:View) : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(editable: Editable?) {
            val text = editable.toString()
            when(view.id) {
                R.id.et_search_concert -> {
                    Log.d("textWatcher", text)
                    getDataConcertByArtist(text.uppercase())
                    getDataConcertByArtist(text.lowercase())
                }
            }
        }

    }

    override fun onClickListenerItem(data: ConcertResponse.Concert) {
        TODO("Not yet implemented")
    }


}