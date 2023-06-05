package com.example.ratemyculture.feature.main.maps

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.ratemyculture.R
import com.example.ratemyculture.data.model.place.Place
import com.example.ratemyculture.data.model.place.PlacesReader
import com.example.ratemyculture.feature.upload.UploadActivity
import com.example.ratemyculture.util.drawableToBitmapDescriptor
import com.example.ratemyculture.util.openAppSystemSettings
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.rxjava3.core.Observable
import java.io.File
import java.io.IOException
import java.util.Date
import java.util.concurrent.TimeUnit


class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private val places: List<Place> by lazy {
        PlacesReader(requireContext()).read()
    }
    private val viewModel: MapFragmentVM by viewModels()

    companion object {
        val TAG = "com.example.ratemyculture.feature.main.maps.MapsFragment"
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest


    private val openCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {

            }
        }

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            //send to upload activity
            //start new activity after 1 second
            val rx = Observable.timer(1, TimeUnit.SECONDS)
                .subscribe {
                    val intent = Intent(context, UploadActivity::class.java)
                    intent.putExtra("imageUri", uri)
                    startActivity(intent)
                }
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(
            R.id.map_fragment
        ) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

    }


    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            addMyLocationMarker()
            createLocationRequest()
            startLocationUpdates()
        } else {
            requestLocationPermission()
        }
        addMarkers()
        listenMarkers()
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval =
                10000 // Sets the desired interval for active location updates, in milliseconds.
            fastestInterval =
                5000 // Sets the fastest rate for active location updates, in milliseconds.
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun addMyLocationMarker() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val userLatLng = LatLng(location.latitude, location.longitude)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))
            }
        }
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        googleMap.isMyLocationEnabled = true
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun addMarkers() {
        val icon = MarkerOptions().drawableToBitmapDescriptor(R.drawable.athens, requireContext())
        places.forEach { place ->
            googleMap.addMarker(
                MarkerOptions()
                    .icon(icon)
                    .title(place.name)
                    .position(place.latLng)
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addMyLocationMarker()
            }
        }
    }

    private fun startLocationUpdates() {
        // update users location every 10 seconds
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0 ?: return
                for (location in p0.locations) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                }
            }
        }
    }
    /*
        private fun startLocationUpdates() {
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult ?: return
                    locationResult.locations.forEach { location ->
                        val latLng = LatLng(location.latitude, location.longitude)
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    }
                }
            }

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
        */

    private fun listenMarkers() {
        computeDistanceOfUserAndMarker()
    }

    private fun computeDistanceOfUserAndMarker() {
        googleMap.setOnMarkerClickListener { clickedMarker ->
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                clickedMarker.showInfoWindow()
                val fusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(requireActivity())
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        val userLatLng = LatLng(location.latitude, location.longitude)
                        val distance = FloatArray(1)
                        println("marker: ${clickedMarker.position}")
                        println("user: $userLatLng")
                        Location.distanceBetween(
                            userLatLng.latitude,
                            userLatLng.longitude,
                            clickedMarker.position.latitude,
                            clickedMarker.position.longitude,
                            distance
                        )
                        //todo distance şimdilik 1000 sonra değiştirirsin.
                        if (distance[0] < 1000) {
                            val builder = AlertDialog.Builder(requireContext())
                            builder.setMessage("Do you want to check-in [${clickedMarker.title}] here?")
                                .setCancelable(false)
                                .setPositiveButton("Yes") { _, _ ->
                                    // şu konumda checkin yapıldı longitude ve latitude bilgileri ile ve places name ile

                                    //todo dont update userpoint for now
                                    /*
                                    viewModel.userPoints.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
                                        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                                            Toast.makeText(requireContext(),"Point increased! New point: ${viewModel.userPoints.get()}",Toast.LENGTH_SHORT).show()
                                        }
                                    })

                                     */

                                    showOptionsDialog()


                                    println("checkin: ${clickedMarker.title}")
                                    //  viewModel.checkIn(clickedMarker.position.latitude, clickedMarker.position.longitude)
                                }
                                .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
                            val alert = builder.create()
                            alert.show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "You are far away from the location!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            clickedMarker.tag = 1
            true
        }
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            context?.openAppSystemSettings()
        } else {
            dispatchTakePictureIntent()
        }
    }


    val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireContext().packageManager).also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    //TODO: handle error
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        requireContext().packageName + ".fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // The photo was taken and saved to the file specified in the Intent, so you can now upload it to your server or do whatever you want with it.
            val imageFile = File(currentPhotoPath)
            // Pass the imageFile to your upload activity
            val uploadIntent = Intent(context, UploadActivity::class.java)
            uploadIntent.putExtra("imageFile", imageFile)
            startActivity(uploadIntent)
        }
    }

    private fun showOptionsDialog() {
        val items = arrayOf<CharSequence>("Camera", "Gallery")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose an option")
        builder.setItems(items) { dialog, item ->
            if (item == 0) {
                openCamera()
            } else if (item == 1) {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }
        builder.show()
    }
}
