package com.example.ratemyculture.feature.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.ratemyculture.R
import com.example.ratemyculture.databinding.FragmentProfileBinding
import com.example.ratemyculture.util.firebaseAuth

class ProfileFragment : Fragment() {


    private val viewModel by lazy {
        ProfileFragmentVM()
    }
    companion object{
        val TAG = "ProfileFragment"
    }
    private var uid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("ProfileFragment.onCreate")
      //  viewModel.uid.let { it.get()?.let { it1 -> viewModel.getCurrentUserProfileData(it1) } }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        println("ProfileFragment.onCreateView")
        //inflate data binding
        val binding = DataBindingUtil.inflate<FragmentProfileBinding>(
            inflater, R.layout.fragment_profile, container, false
        )
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth.currentUser?.let { viewModel.getCurrentUserProfileData(it.uid,requireContext()) }
    }
}