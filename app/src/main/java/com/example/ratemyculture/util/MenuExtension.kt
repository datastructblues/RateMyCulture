package com.example.ratemyculture.util

import android.content.Intent
import android.util.Log
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.ratemyculture.R
import com.example.ratemyculture.feature.main.MainActivity
import com.example.ratemyculture.feature.main.maps.MapsFragment
import com.example.ratemyculture.feature.main.profile.ProfileFragment
import com.example.ratemyculture.feature.main.profile.ProfileFragmentVM
import com.example.ratemyculture.feature.signin.SignInActivity
import de.hdodenhof.circleimageview.CircleImageView

/*

fun MainActivity.onNavigationButtonClicked(item:MenuItem):Boolean{
    val profileFragment = ProfileFragment()
    val mapsFragment = com.example.ratemyculture.feature.main.maps.MapsFragment()
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
            if(supportFragmentManager.findFragmentById(R.id.center) is com.example.ratemyculture.feature.main.maps.MapsFragment){
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


 */

private var activeFragment: Fragment? = null

fun MainActivity.onNavigationButtonClicked(item: MenuItem): Boolean {
    val profileFragment = supportFragmentManager.findFragmentByTag(ProfileFragment.TAG)
        ?: ProfileFragment()
    val mapsFragment = supportFragmentManager.findFragmentByTag(MapsFragment.TAG)
        ?: MapsFragment()

    if (activeFragment == null) {
        supportFragmentManager.beginTransaction()
            .add(R.id.center, profileFragment, ProfileFragment.TAG)
            .hide(profileFragment)
            .add(R.id.center, mapsFragment, MapsFragment.TAG)
            .hide(mapsFragment)
            .commit()

        activeFragment = profileFragment
    }

    return when (item.itemId) {
        R.id.profile -> {
            if (activeFragment == profileFragment) {
                return false
            }

            supportFragmentManager.beginTransaction()
                .show(profileFragment)
                .hide(activeFragment!!)
                .commit()

            activeFragment = profileFragment
            true
        }

        R.id.maps -> {
            if (activeFragment == mapsFragment) {
                return false
            }

            supportFragmentManager.beginTransaction()
                .show(mapsFragment)
                .hide(activeFragment!!)
                .commit()

            activeFragment = mapsFragment
            true
        }

        else -> false
    }
}

fun ProfileFragment.onMenuButtonClicked(
    item: MenuItem,
    pickMedia: ActivityResultLauncher<PickVisualMediaRequest>,
    viewModel: ProfileFragmentVM
): Boolean {
    return when (item.itemId) {
        R.id.action_add -> {
            //update profile picture
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            true
        }

        R.id.action_take -> {
            //todo take
            true
        }

        R.id.action_delete -> {
            //todo delete
            viewModel.photoUrl.set("null")
            viewModel.updateProfilePicture(
                firebaseAuth.currentUser!!.uid,
                viewModel.photoUrl.get()!!)
            true
        }

        else -> false
    }
}

fun ProfileFragment.onProfileMenuClicked(item: MenuItem, signInLauncher: ActivityResultLauncher<Intent>): Boolean {
    return when (item.itemId) {
        R.id.sign_out -> {
            firebaseAuth.signOut()
            Log.d("ProfileFragment", "User signed out")
            //clear fragment stack
            requireActivity().finish()
            activeFragment = null
            //todo navigate to login
            signInLauncher.launch(Intent(requireContext(), SignInActivity::class.java))
            true
        }
        else -> false
    }
}
