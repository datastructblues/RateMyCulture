package com.example.ratemyculture.feature.main.post

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.ratemyculture.util.fbDatabase

class PostFragmentVM: ViewModel() {

    val photoUrl = ObservableField<String>()
    var username = ObservableField<String>()
    var caption = ObservableField<String>()
    var date = ObservableField<String>()
    var profilePicture = ObservableField<String>()

    fun getCurrentPost(uid: String, context: Context) {
        uid.let {
            val userData = fbDatabase.collection("users").document(uid)
            userData.get().addOnSuccessListener { document ->
                if (document != null) {
                    val username = document.get("username").toString()
                    val photoUrl = document.get("photo_url").toString()
                    this.username.set(username)
                    this.profilePicture.set(photoUrl)
                } else {
                    println("No such document")
                }
            }.addOnFailureListener { exception ->
                exception.localizedMessage?.let {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}