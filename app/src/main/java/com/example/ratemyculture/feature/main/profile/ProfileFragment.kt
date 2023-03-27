package com.example.ratemyculture.feature.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ratemyculture.R
import com.example.ratemyculture.util.firebaseAuth
import com.google.android.gms.maps.SupportMapFragment

class ProfileFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCurrentUserDataFromFirebase()

    }

    private fun getCurrentUserDataFromFirebase(){
        firebaseAuth.currentUser?.let {
            val name = it.displayName
            val email = it.email
            val photoUrl = it.photoUrl
            val uid = it.uid
        }
    }
}