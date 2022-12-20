package com.example.concert_app.view.main.fragment.home

import android.Manifest
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.concert_app.R
import com.example.concert_app.databinding.FragmentHomeBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.*


class HomeFragment : Fragment() {


    companion object {
        private const val TAG = "SearchConcertFragment"
        private const val REQUEST_CHECK_SETTINGS = 12345
    }


    private lateinit var binding: FragmentHomeBinding

    private lateinit var chipGroup: ChipGroup

    private lateinit var chipAll: Chip

    private lateinit var btnSearch: MaterialButton

    private lateinit var currentLocation: TextView

    private lateinit var locationManager: LocationManager
    private var gpsStatus = false
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


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

        // Chcek GPS Status
        checkGpsStatus()

        // Current Location
        getCurrentLocation()

        return binding.root
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

    private fun checkGpsStatus() {
        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (gpsStatus) {
            Log.i(TAG, "GPS Is enabled")
        } else {
            Log.w(TAG, "GPS Is Not enabled")
            turnOnGPS()
        }
    }

    /**
     * Enable gps system
     */
    private fun turnOnGPS() {
        val locationRequest = LocationRequest.create().apply {
            interval = 2000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        val task: Task<LocationSettingsResponse> =
            settingsClient.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            Log.d(TAG, "Location Settings State : ${it.locationSettingsStates}")
        }
        task.addOnFailureListener {
            if (it is ResolvableApiException) {
                try {
                    it.startResolutionForResult(requireActivity(), REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.d(TAG, sendEx.localizedMessage!!)
                }
            }
        }
    }

    private fun getCurrentLocation() {
        // Get current location
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            val latitude = location?.latitude
            val longitude = location?.longitude
            val locationCoordinate = "$latitude, $longitude"
            Log.d(TAG, "Location Coordinate $locationCoordinate")

            if (latitude != null && longitude != null) {
                Log.d(TAG, "Location Coordinate $locationCoordinate")

                val geocoder = Geocoder(requireActivity(), Locale.getDefault())
                val address: Address?
                val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)

                address = addresses[0]
                val fullAddress = address.getAddressLine(0)
                val subDistrict = address.locality
                val province = address.adminArea
                val country = address.countryName
                val postalCode = address.postalCode
                val knownName = address.featureName
                val urbanVillage = address.subLocality
                val countryCode = address.countryCode
                val districtOrRegency = address.subAdminArea
                val streetName = address.thoroughfare

                currentLocation.text = "$urbanVillage, $subDistrict"


                Log.i(TAG, "Urban Village(Kelurahan): $urbanVillage")
                Log.i(TAG, "Country Code(Code Negara): $countryCode")
                Log.i(TAG, "Phone: ${address.phone}")
                Log.i(TAG, "Premises: ${address.premises}")
                Log.i(TAG, "District/Regency(Kota/Kabupaten): $districtOrRegency")
                Log.i(TAG, "subThoroughfare: ${address.subThoroughfare}")
                Log.i(TAG, "Street Name(Nama Jalan): $streetName")
                Log.i(TAG, "Url: ${address.url}")
                Log.i(TAG, "Max Address: ${address.maxAddressLineIndex}")
                Log.i(TAG, "Address: $address")
                Log.i(TAG, "Address: $addresses")
                Log.i(
                    TAG,
                    "======================================================================="
                )

                Log.i(TAG, "FullAddress(Alamat Lengkap): $fullAddress")
                Log.i(TAG, "Sub-District(Kecamatan): $subDistrict")
                Log.i(TAG, "Province(Provinsi): $province")
                Log.i(TAG, "Country(Negara): $country")
                Log.i(TAG, "postal code(Kode Wilayah): $postalCode")
                Log.i(TAG, "knowName: $knownName")

            } else {
                Log.d(TAG, "Location Coordinate $locationCoordinate")
            }


        }
    }
}