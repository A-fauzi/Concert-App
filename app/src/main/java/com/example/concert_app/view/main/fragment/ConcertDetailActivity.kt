package com.example.concert_app.view.main.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.net.toUri
import com.example.concert_app.R
import com.example.concert_app.databinding.ActivityConcertDetailctivityBinding
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style
import com.squareup.picasso.Picasso
import java.lang.Exception

class ConcertDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConcertDetailctivityBinding


    private var mapView: MapView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(this, getString(R.string.access_token))

        binding = ActivityConcertDetailctivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retrieveData()

        mapView = findViewById<MapView>(R.id.mapView)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync { mapBoxMap ->
            val currentCameraPosition = mapBoxMap.cameraPosition
            currentCameraPosition.zoom
            currentCameraPosition.tilt

            val position = CameraPosition.Builder()
                .target(LatLng( -6.184655825881868,106.81210251827447))
                .zoom(10.0)
                .tilt(20.0)
                .build()

            mapBoxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 3000)

            val latLngBounds = LatLngBounds.Builder()
                .include(LatLng(-6.184655825881868,106.81210251827447))
                .include(LatLng(-6.248820935608907, 106.96522445759))
                .build()
            mapBoxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10), 3000)
            mapBoxMap.setStyle(Style.MAPBOX_STREETS) {

            }
        }



    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    private fun retrieveData() {
        val thumbnail = intent.extras?.getString("photo_url")
        val title = intent.extras?.getString("title")
        val dateTime = intent.extras?.getString("date_time")
        val locName = intent.extras?.getString("location_name")
        val desc = intent.extras?.getString("description")


        Picasso.get()
            .load(thumbnail)
            .placeholder(R.drawable.image_ini)
            .error(R.mipmap.ic_launcher)
            .into(binding.ivThumbnail)

        binding.tvTitle.text = title
        binding.tvDateTime.text = dateTime
        binding.tvLocationName.text = locName
        binding.tvDescription.text = desc
    }
}