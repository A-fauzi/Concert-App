package com.example.concert_app.view.main.fragment


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.concert_app.R
import com.example.concert_app.databinding.ActivityConcertDetailctivityBinding
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.Icon
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.IconFactory.getInstance
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager
import com.squareup.picasso.Picasso


class ConcertDetailActivity : AppCompatActivity(), OnMapReadyCallback,
    MapboxMap.OnMapClickListener {

    private lateinit var binding: ActivityConcertDetailctivityBinding

    private var mapView: MapView? = null
    private var mapboxMap: MapboxMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(this, getString(com.example.concert_app.R.string.access_token))

        binding = ActivityConcertDetailctivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val latitude = intent.extras?.getString("latitude")
        val longitude = intent.extras?.getString("longitude")
        val locName = intent.extras?.getString("location_name")

        retrieveData()

        mapView = findViewById(com.example.concert_app.R.id.mapView)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)

        mapView?.getMapAsync {
            val icon = IconFactory.getInstance(this)
            val drawable: Drawable = resources.getDrawable(R.drawable.icon_marker_map)
            drawable.setBounds(0,0, 50, 50)


            it.addMarker(
                MarkerOptions()
                    .position(LatLng(latitude!!.toDouble(), longitude!!.toDouble()))
                    .title(locName)
                    .icon(icon.fromResource(R.drawable.icons8_place_marker_48))
            )
        }
    }

    private fun retrieveData() {
        val thumbnail = intent.extras?.getString("photo_url")
        val title = intent.extras?.getString("title")
        val dateTime = intent.extras?.getString("date_time")
        val locName = intent.extras?.getString("location_name")
        val desc = intent.extras?.getString("description")

        Picasso.get()
            .load(thumbnail)
            .placeholder(com.example.concert_app.R.drawable.image_ini)
            .error(com.example.concert_app.R.mipmap.ic_launcher)
            .into(binding.ivThumbnail)

        binding.tvTitle.text = title
        binding.tvDateTime.text = dateTime
        binding.tvLocationName.text = locName
        binding.tvDescription.text = desc
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
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

    override fun onMapReady(mapboxMap: MapboxMap) {
        this@ConcertDetailActivity.mapboxMap = mapboxMap

        mapboxMap.setStyle(Style.MAPBOX_STREETS) { // Toast instructing user to tap on the map
            Toast.makeText(
                this@ConcertDetailActivity,
                getString(com.example.concert_app.R.string.tap_on_map_instruction),
                Toast.LENGTH_LONG
            ).show()
            mapboxMap.addOnMapClickListener(this@ConcertDetailActivity)
        }
    }

    override fun onMapClick(point: LatLng): Boolean {
        // Toast instructing user to tap on the map
        // Toast instructing user to tap on the map
        Toast.makeText(
            this@ConcertDetailActivity,
            getString(com.example.concert_app.R.string.tap_on_map_instruction),
            Toast.LENGTH_LONG
        ).show()

        val latitude = intent.extras?.getString("latitude")
        val longitude = intent.extras?.getString("longitude")

        val position: CameraPosition = CameraPosition.Builder()
            .target(
                LatLng(
                    latitude?.toDouble() ?: -6.175098651934187,
                    longitude?.toDouble() ?: 106.82617875036401
                )
            ) // Sets the new camera position
            .zoom(17.0) // Sets the zoom
            .bearing(180.0) // Rotate the camera
            .tilt(30.0) // Set the camera tilt
            .build() // Creates a CameraPosition from the builder


        mapboxMap!!.animateCamera(
            CameraUpdateFactory
                .newCameraPosition(position), 7000
        )

        return true
    }


}