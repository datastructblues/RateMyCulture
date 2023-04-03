package com.example.ratemyculture.feature.main.profile

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.ratemyculture.util.fbDatabase

class ProfileFragmentVM: ViewModel(){

    //livedata for user profile data
    val username = ObservableField<String>()
    val email = ObservableField<String>()
    val point = ObservableField<String>()
    val photoUrl = ObservableField<String>()


    fun getCurrentUserProfileData(uid:String,context: Context) {
        uid.let {
            val userData = fbDatabase.collection("googleUsers").document(uid)
            userData.get().addOnSuccessListener { document ->
                if (document != null) {
                    val username = document.get("username").toString()
                    val email = document.get("email").toString()
                    val point = document.get("point").toString()
                    val photoUrl = document.get("photo_url").toString()
                    this.username.set(username)
                    this.email.set(email)
                    this.point.set(point)
                    this.photoUrl.set(photoUrl)
                    context.let { context ->
                        Toast.makeText(
                            context,
                            "Username: $username, Email: $email, Point: $point, PhotoUrl: $photoUrl",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    println("No such document")
                }
            }.addOnFailureListener { exception ->
            }
        }
    }
}