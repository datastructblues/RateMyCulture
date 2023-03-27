package com.example.ratemyculture.feature.main

import com.example.ratemyculture.core.base.BaseNavigator
import com.example.ratemyculture.core.base.BaseViewModel
import com.example.ratemyculture.data.model.place.Place
import com.example.ratemyculture.data.model.place.PlacesReader
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions

class MainVM : BaseViewModel<BaseNavigator>() {

    lateinit var mMap: GoogleMap
     val places: List<Place> by lazy {
        PlacesReader(navigator?.getContext()!!).read()
    }

     fun addMarkers(googleMap: GoogleMap) {
        places.forEach { place ->
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .title(place.name)
                    .position(place.latLng)
            )
            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    place.latLng,
                    10f
                )
            )
        }
    }
}