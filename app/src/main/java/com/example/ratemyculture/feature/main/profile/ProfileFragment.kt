package com.example.ratemyculture.feature.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.ratemyculture.R
import com.example.ratemyculture.databinding.FragmentProfileBinding
import com.example.ratemyculture.util.firebaseAuth
import com.example.ratemyculture.util.onMenuButtonClicked
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment() {


    private val viewModel by lazy {
        ProfileFragmentVM()
    }

    companion object {
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
        firebaseAuth.currentUser?.let {
            viewModel.getCurrentUserProfileData(
                it.uid,
                requireContext()
            )
        }
        openDropdown()
    }

     private fun openDropdown() {
        val imageView = requireView().findViewById<ImageView>(R.id.add_profile_picture)
        imageView?.setOnClickListener {
            println("clicked!")
            val popupMenu = PopupMenu(requireContext(), imageView)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

            val photo = viewModel.photoUrl.get()

            if (photo == null) {
                val menuItem = popupMenu.menu.findItem(R.id.action_delete)
                menuItem.isEnabled = false
            }
            popupMenu.setOnMenuItemClickListener { menuItem ->
                onMenuButtonClicked(menuItem)
            }
            popupMenu.show()
        }
    }
}