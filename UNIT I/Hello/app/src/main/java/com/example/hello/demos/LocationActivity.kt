package com.example.hello.demos

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.MaterialToolbar
import com.example.hello.R

class LocationActivity : AppCompatActivity() {

    private lateinit var tvCoords: TextView
    private lateinit var locationManager: LocationManager

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            fetchDeviceLocation()
        } else {
            Toast.makeText(this, "Location permission denied!", Toast.LENGTH_SHORT).show()
            tvCoords.text = "Permission denied by user."
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = "Location Page"
        toolbar.setNavigationOnClickListener { finish() }

        tvCoords = findViewById(R.id.tvLocationCoords)
        val btnGet = findViewById<Button>(R.id.btnGetLocation)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        btnGet.setOnClickListener {
            checkPermissionAndGetLocation()
        }
    }

    private fun checkPermissionAndGetLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fetchDeviceLocation()
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun fetchDeviceLocation() {
        try {
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGpsEnabled && !isNetworkEnabled) {
                tvCoords.text = "Please enable GPS / Location services on device."
                return
            }

            val lastKnownGps = if (isGpsEnabled) {
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            } else null

            val lastKnownNetwork = if (isNetworkEnabled) {
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            } else null

            val location: Location? = lastKnownGps ?: lastKnownNetwork

            if (location != null) {
                tvCoords.text = "Latitude: ${location.latitude}\nLongitude: ${location.longitude}\nProvider: ${location.provider}"
            } else {
                tvCoords.text = "Fetching live coordinates..."
                val provider = if (isGpsEnabled) LocationManager.GPS_PROVIDER else LocationManager.NETWORK_PROVIDER
                locationManager.requestSingleUpdate(
                    provider,
                    object : LocationListener {
                        override fun onLocationChanged(loc: Location) {
                            tvCoords.text = "Latitude: ${loc.latitude}\nLongitude: ${loc.longitude}\nProvider: ${loc.provider}"
                        }
                        override fun onProviderEnabled(provider: String) {}
                        override fun onProviderDisabled(provider: String) {}
                    },
                    mainLooper
                )
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            tvCoords.text = "Location Permission Error."
        }
    }
}
