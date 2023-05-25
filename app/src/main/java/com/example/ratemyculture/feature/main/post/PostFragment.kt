package com.example.ratemyculture.feature.main.post

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.ratemyculture.R
import com.example.ratemyculture.data.model.sharings.Sharing
import com.example.ratemyculture.databinding.FragmentPostBinding
import com.example.ratemyculture.util.firebaseAuth

class PostFragment() : Fragment() {

    private var sharing:Sharing? = null
    private lateinit var binding: FragmentPostBinding

    private val viewModel by lazy {
        PostFragmentVM()
    }

    companion object {
        const val TAG = "PostFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentPostBinding>(
            inflater, R.layout.fragment_post, container, false
        )
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth.currentUser?.uid?.let {
            viewModel.getCurrentPost(
                it,
                requireContext()
            )
        }
        setPostData()
    }

    fun setSharingData(sharing: Sharing) {
        this.sharing = sharing
    }
//todo set local later

    private fun setPostData(){
        sharing?.let {
            viewModel.photoUrl.set(it.photoUrl)
            viewModel.username.set(it.userId)
            viewModel.caption.set(it.caption)
            viewModel.date.set(it.uploadDate.toString())
        }
    }
}