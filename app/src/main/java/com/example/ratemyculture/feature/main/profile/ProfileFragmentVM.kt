package com.example.ratemyculture.feature.main.profile

import android.graphics.Bitmap
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.ratemyculture.util.fbDatabase

class ProfileFragmentVM: ViewModel(){

    var username= ObservableField<String>()
    var email= ObservableField<String>()
    var point= ObservableField<String>()
    var photoUrl= ObservableField<String>()
    var bitmap = ObservableField<Bitmap>()
    var uid = ObservableField<String>()


    fun getCurrentUserProfileData(uid:String) {
        println("bitmapdata: ${bitmap.get()}")
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
                    this.uid.set(uid)
                    /*
                    navigator?.getContext()?.let { context ->
                        navigator?.showToast(
                            context,
                            "Username: $username, Email: $email, Point: $point, PhotoUrl: $photoUrl",
                            false
                        )
                    }

                     */
                } else {
                    /*
                    navigator?.getContext()?.let { context ->
                        navigator?.showToast(
                            context,
                            "No such document",
                            false
                        )
                    }

                     */
                }
            }.addOnFailureListener { exception ->
                /*
                navigator?.getContext()?.let { context ->
                    navigator?.showToast(
                        context,
                        "get failed with $exception",
                        false
                    )
                }
            }

                 */
            }
        }
    }
}