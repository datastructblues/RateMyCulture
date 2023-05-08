package com.example.ratemyculture.feature.main.maps

import android.net.Uri
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.ratemyculture.util.fbDatabase
import com.example.ratemyculture.util.fbStorage
import com.example.ratemyculture.util.firebaseAuth
import com.example.ratemyculture.util.getDocument
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import java.io.File

class MapFragmentVM : ViewModel() {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    val userPoints = ObservableField<Int>(0)

    fun updateUserPoint() {
        val userData = getDocument("users", firebaseAuth.currentUser?.uid.toString())
        userData.get().addOnSuccessListener { document ->
            if (document != null) {
                val point = document.get("point").toString().toInt()
                userData.update("point", point.plus(10))
                userPoints.set(point.plus(10))
            } else {
                println("No such document")
            }
        }.addOnFailureListener { exception ->
        }
    }

    fun createImageFile(): Uri? {
        val storageRef = fbStorage.reference
        val imageRef = storageRef.child("images/${firebaseAuth.currentUser?.uid.toString()}")
        val file = File.createTempFile("images", "jpg")
        val uri = Uri.fromFile(file)
        val uploadTask = imageRef.putFile(uri)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener {
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
        return uri
    }
}