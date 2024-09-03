package com.example.tire.di

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

/*
class LocationLiveData(var context: Context): LiveData<LocationDetails>() {

    //add dependency implementation "com.google.android.gms:play-services-maps:18.0.2"
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)


    override fun onActive() {
        super.onActive()



        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) { // alse geen permissie hebben just return, anders voer functie location uit

            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
                location -> location.also {
            setLocationData(it)


        }

        }
    }

    internal fun startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

    }

    private fun setLocationData(location: Location?) {

        location?.let { it ->
            //value is observed in LiveData
            value = LocationDetails(
                longitude = it.longitude.toString(),
                lattitude = it.latitude.toString()
            )

        }
        println("value $value")


    }


    override fun onInactive() {
        super.onInactive()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            println("we have a new location result")
            locationResult ?: return //als er een result is dan prima, zo niet dan just return (elvis operator)
            for (location in locationResult.locations) {
                setLocationData(location = location)

            }
        }
    }

    companion object {

        val ONE_MINUTE: Long = 1000
        @RequiresApi(Build.VERSION_CODES.S)
        val locationRequest : com.google.android.gms.location.LocationRequest = com.google.android.gms.location.LocationRequest.create().apply {
            interval = ONE_MINUTE
            fastestInterval = ONE_MINUTE/4
            priority = LocationRequest.QUALITY_HIGH_ACCURACY
        }


    }

}
*/

