package com.example.ratemyculture.feature.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.ratemyculture.R
import com.example.ratemyculture.util.firebaseAuth
import com.google.android.gms.maps.SupportMapFragment

class ProfileFragment : Fragment() {


    private val viewModel by lazy {
        ProfileFragmentVM()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("ProfileFragment.onCreate")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        println("ProfileFragment.onCreateView")
        if (arguments != null){
            viewModel.uid.set(arguments?.getString("uid")!!)
        }
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uid.let { it.get()?.let { it1 -> viewModel.getCurrentUserProfileData(it1) } }
    }
}