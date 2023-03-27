package com.example.ratemyculture.util

import android.view.MenuItem
import com.example.ratemyculture.R
import com.example.ratemyculture.feature.main.MainActivity
import com.example.ratemyculture.feature.main.maps.MapsFragment
import com.example.ratemyculture.feature.main.profile.ProfileFragment


fun MainActivity.onNavigationButtonClicked(item:MenuItem):Boolean{
    val profileFragment = ProfileFragment()
    val mapsFragment = MapsFragment()
    return when(item.itemId){
        R.id.profile -> {
            if (supportFragmentManager.findFragmentById(R.id.center) is ProfileFragment){
                return false
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.center, profileFragment)
                .commit()
            true
        }
        R.id.maps -> {
            if(supportFragmentManager.findFragmentById(R.id.center) is MapsFragment){
                return false
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.center, mapsFragment)
                .commit()
            true
        }
        else -> false
    }
}
