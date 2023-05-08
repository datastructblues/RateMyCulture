package com.example.ratemyculture.feature.upload

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.ratemyculture.core.base.BaseNavigator
import com.example.ratemyculture.core.base.BaseViewModel
import com.example.ratemyculture.util.fbStorage
import com.example.ratemyculture.util.firebaseAuth
import java.io.File

class UploadVM: BaseViewModel<BaseNavigator>() {

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