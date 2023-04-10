package com.example.ratemyculture.feature.main

import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.Menu
import androidx.databinding.library.baseAdapters.BR
import com.example.ratemyculture.R
import com.example.ratemyculture.core.base.BaseActivity
import com.example.ratemyculture.core.base.BaseNavigator
import com.example.ratemyculture.databinding.ActivityMainBinding
import com.example.ratemyculture.feature.main.profile.ProfileFragment
import com.example.ratemyculture.util.onNavigationButtonClicked
import com.google.android.gms.maps.GoogleMap


class MainActivity : BaseActivity<ActivityMainBinding,MainVM>(),BaseNavigator {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_main
    override val viewModel: MainVM = MainVM()

    private var mBinding: ActivityMainBinding? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = super.viewDataBinding
        viewModel.navigator = this
        startProfileActivity()
        onNavigationClicks()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.navigation_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun startProfileActivity() {
        val profileFragment = ProfileFragment()
        //    sendDataToProfileFragment(profileFragment)
        supportFragmentManager.beginTransaction()
            .replace(R.id.center, profileFragment)
            .commit()
    }

    private fun onNavigationClicks() {
        mBinding?.bottomNavigation?.setOnNavigationItemSelectedListener { menuItem ->
            onNavigationButtonClicked(menuItem)
        }
    }

    /*
    private fun sendDataToProfileFragment(fragment: ProfileFragment){
        val bundle = Bundle()
        bundle.putString("uid",viewModel.uid.get())
        fragment.arguments = bundle

     */

}