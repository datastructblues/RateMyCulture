package com.example.ratemyculture.feature.main.profile

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ratemyculture.R
import com.example.ratemyculture.data.model.sharings.Sharing
import com.example.ratemyculture.databinding.FragmentProfileBinding
import com.example.ratemyculture.feature.main.post.PostFragment
import com.example.ratemyculture.util.firebaseAuth
import com.example.ratemyculture.util.onMenuButtonClicked
import com.example.ratemyculture.util.onProfileMenuClicked
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment() {


    private val viewModel by lazy {
        ProfileFragmentVM()
    }

    companion object {
        const val TAG = "ProfileFragment"
    }

    private val currentUserId = firebaseAuth.currentUser?.uid
    private lateinit var binding: FragmentProfileBinding
    private var adapter: SharingAdapter? = null

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            val imageView = requireView().findViewById<CircleImageView>(R.id.profile_image)
            imageView.setImageURI(uri)
            val bitmap = imageView.drawable.toBitmap()

            viewModel.uploadUserPictureToStorage(currentUserId, bitmap)

            //todo  viewModel.updateCurrentUserProfilePicture()

        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    val signInActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // There are no request codes
                val data = result.data
                startActivity(data)
                // Handle the Intent
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
        binding = DataBindingUtil.inflate<FragmentProfileBinding>(
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
        openProfileDrawerMenu()
        initRecyclerView()
        swipeRefresh()
    }

    /*
        private fun initRecyclerView() {
            val itemList =  viewModel.getUserSharings()
            val recyclerView: RecyclerView = binding.recyclerView
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = SharingAdapter(itemList)
            recyclerView.adapter = adapter
        }

     */
    private fun initRecyclerView() {
        viewModel.getUserSharings { itemList ->
            var recyclerView: RecyclerView = binding.recyclerView
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = SharingAdapter(itemList) {
                onPhotoItemClick(it)
            }
            recyclerView.adapter = adapter
        }
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
                onMenuButtonClicked(menuItem, pickMedia)
            }
            popupMenu.show()
        }
    }

    private fun openProfileDrawerMenu() {
        binding.profileMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.END)
            binding.navigationView.setNavigationItemSelectedListener { menuItem ->
                onProfileMenuClicked(menuItem, signInActivityLauncher)
            }
            binding.navHeader.close.setOnClickListener {
                binding.drawerLayout.closeDrawer(GravityCompat.END)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun swipeRefresh() {
        binding.refresher.setOnRefreshListener {
            viewModel.getUserSharings { itemList ->
                adapter?.updateList(itemList)
            }
            binding.refresher.isRefreshing = false
        }
    }

    private fun onPhotoItemClick(sharing: Sharing) {
        // Your action when an item is clicked
        println("itemclicked ${sharing.photoUrl}")
        val postFragment = PostFragment()
        postFragment.setSharingData(sharing)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.center, postFragment)
            .addToBackStack(null)
            .commit()
    }
}
