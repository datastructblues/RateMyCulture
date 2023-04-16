package com.example.ratemyculture.feature.main.profile

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
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

   private val currentUserId = firebaseAuth.currentUser?.uid

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                val imageView = requireView().findViewById<CircleImageView>(R.id.profile_image)
                imageView.setImageURI(uri)
                val bitmap = imageView.drawable.toBitmap()

                viewModel.uploadUserPictureToStorage(currentUserId, bitmap,requireContext())

              //todo  viewModel.updateCurrentUserProfilePicture()

            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

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
        currentUserId?.let {
            viewModel.getCurrentUserProfileData(
                it,
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
                onMenuButtonClicked(menuItem,pickMedia)
            }
            popupMenu.show()
        }
    }
}