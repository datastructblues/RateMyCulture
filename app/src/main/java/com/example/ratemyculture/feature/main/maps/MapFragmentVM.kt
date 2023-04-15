package com.example.ratemyculture.feature.main.maps

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap

class MapFragmentVM: ViewModel(){

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    var userPoints = ObservableField<Int>()
}