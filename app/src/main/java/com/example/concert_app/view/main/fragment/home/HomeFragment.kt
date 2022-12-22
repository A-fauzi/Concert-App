package com.example.concert_app.view.main.fragment.home

import android.Manifest
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.concert_app.R
import com.example.concert_app.data.whatsapp.ServiceImplement
import com.example.concert_app.databinding.FragmentHomeBinding
import com.example.concert_app.utils.FirebaseServiceInstance
import com.example.concert_app.utils.Preference.loadData
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.io.IOException
import java.util.*


class HomeFragment : Fragment() {


    companion object {
        private const val TAG = "SearchConcertFragment"
        private const val REQUEST_CHECK_SETTINGS = 12345
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var binding: FragmentHomeBinding

    private lateinit var chipGroup: ChipGroup

    private lateinit var chipAll: Chip

    private lateinit var btnSearch: MaterialButton

    private lateinit var currentLocation: TextView




    private fun initView() {
        chipGroup = binding.chipGroup
        chipAll = binding.chipAllGenres
        btnSearch = binding.btnSearch
        currentLocation = binding.tvCurrentLocation
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        initView()

        replaceFragment(AllGenresFragment())

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            val latitude = location?.latitude
            val longitude = location?.longitude
            val locationCoordinate = "$latitude, $longitude"

            if (latitude != null && longitude != null) {
                getAddress(latitude, longitude)
            }

        }


        return binding.root
    }


    private fun getAddress(lat: Double, lng: Double) {
        val geocoder = Geocoder(requireActivity(), Locale.getDefault())

        try {
            val address: List<Address> = geocoder.getFromLocation(lat, lng, 1)
            val obj: Address = address[0]
            var add: String = obj.getAddressLine(0)
            add = "${obj.thoroughfare}, ${obj.subLocality}"
            currentLocation.text = add

            Log.d("IGA", "Address: $add")
        }catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT).show()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSearch.setOnClickListener {
            replaceFragment(SearchConcertFragment())
        }

        var state = true

        if (state) chipAll.chipBackgroundColor =
            ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.blue_soft))


        chipGroup.setOnCheckedStateChangeListener { group, checkedId ->
            for (id in checkedId) {
                val chip = group.findViewById<Chip>(id)
                // Do something with the checked chip's ID
                when (chip.text) {
                    "All genres" -> {
                        state = true
                        if (state) chipAll.chipBackgroundColor = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                context!!,
                                R.color.blue_soft
                            )
                        )

                        replaceFragment(AllGenresFragment())

                        Log.d("chip", "Kamu Mengklik All genres")
                    }
                    "Pop" -> {
                        state = false
                        if (!state) chipAll.chipBackgroundColor =
                            ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.gray))

                        replaceFragment(PopFragment())

                        Log.d("chip", "Kamu Mengklik Pop")
                    }
                    "Rock" -> {
                        state = false
                        if (!state) chipAll.chipBackgroundColor =
                            ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.gray))

                        replaceFragment(RockFragment())

                        Log.d("chip", "Kamu Mengklik Rock")
                    }
                    "Indie" -> {
                        state = false
                        if (!state) chipAll.chipBackgroundColor =
                            ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.gray))

                        replaceFragment(IndieFragment())

                        Log.d("chip", "Kamu Mengklik Indie")
                    }
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }
}