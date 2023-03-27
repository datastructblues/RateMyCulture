package com.example.ratemyculture.feature.profile

import android.graphics.Bitmap
import androidx.databinding.ObservableField
import com.example.ratemyculture.core.base.BaseNavigator
import com.example.ratemyculture.core.base.BaseViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileVM : BaseViewModel<BaseNavigator>() {

    private val fbDatabase by lazy { Firebase.firestore }

    var username= ObservableField<String>()
    var email= ObservableField<String>()
    var point= ObservableField<String>()
    var photoUrl= ObservableField<String>()
    var bitmap = ObservableField<Bitmap>()


    fun getCurrentUserProfileData(uid:String){
        println("bitmapdata: ${bitmap.get()}")
        uid?.let {
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
                    navigator?.getContext()?.let { context ->
                        navigator?.showToast(
                            context,
                            "Username: $username, Email: $email, Point: $point, PhotoUrl: $photoUrl",
                            false
                        )
                    }
                } else {
                    navigator?.getContext()?.let { context ->
                        navigator?.showToast(
                            context,
                            "No such document",
                            false
                        )
                    }
                }
            }.addOnFailureListener { exception ->
                navigator?.getContext()?.let { context ->
                    navigator?.showToast(
                        context,
                        "get failed with $exception",
                        false
                    )
                }
            }
        }
    }
}