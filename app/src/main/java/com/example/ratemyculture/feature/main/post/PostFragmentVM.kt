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


    /*
    fun getCurrentPost(uid: String, context: Context) {
        uid.let {
            val userData = fbDatabase.collection("sharings").document(uid)
            userData.get().addOnSuccessListener { document ->
                if (document != null) {
                    val username = document.get("username").toString()
                    val photoUrl = document.get("photo_url").toString()
                    val caption = document.get("caption").toString()
                    val date = document.get("date").toString()
                    this.username.set(username)
                    this.photoUrl.set(photoUrl)
                    this.caption.set(caption)
                    this.date.set(date)
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

     */

}