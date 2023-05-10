package com.example.ratemyculture.feature.upload

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.example.ratemyculture.core.base.BaseNavigator
import com.example.ratemyculture.core.base.BaseViewModel
import com.example.ratemyculture.util.fbStorage
import com.example.ratemyculture.util.firebaseAuth
import java.io.ByteArrayOutputStream
import java.util.UUID


class UploadVM: BaseViewModel<BaseNavigator>() {


    fun uploadImageFile(uri: Uri?) {
        val fileName = UUID.randomUUID().toString()
        val storageRef = fbStorage.reference
        val imageRef = storageRef.child("images/${firebaseAuth.currentUser?.uid}/$fileName")
        val bitmap = MediaStore.Images.Media.getBitmap(navigator?.getContext()?.contentResolver, uri)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = imageRef.putBytes(data)

        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener {
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }

    fun backToMainActivity(){
        navigator?.finishActivity()
    }
}