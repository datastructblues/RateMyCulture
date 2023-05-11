package com.example.ratemyculture.feature.upload

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.example.ratemyculture.core.base.BaseNavigator
import com.example.ratemyculture.core.base.BaseViewModel
import com.example.ratemyculture.data.model.sharings.Sharings
import com.example.ratemyculture.util.fbDatabase
import com.example.ratemyculture.util.fbStorage
import com.example.ratemyculture.util.firebaseAuth
import com.example.ratemyculture.util.getAuthCurrentUserUid
import com.google.firebase.Timestamp
import java.io.ByteArrayOutputStream
import java.util.UUID


class UploadVM: BaseViewModel<BaseNavigator>() {


    fun uploadSharing(uri: Uri?,caption:String) {
        val fileName = UUID.randomUUID().toString()
        val storageRef = fbStorage.reference
        val imageRef = storageRef.child("images/${firebaseAuth.currentUser?.uid}/$fileName")
        val bitmap =
            MediaStore.Images.Media.getBitmap(navigator?.getContext()?.contentResolver, uri)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = imageRef.putBytes(data)

        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener {
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                savePhotoToFirestore(uri.toString(), caption)
            }
        }
    }

    fun backToMainActivity(){
        navigator?.finishActivity()
    }

    private fun savePhotoToFirestore(photoUrl: String, caption: String){
        fbDatabase.collection("sharings").document().set(
            Sharings(
                getAuthCurrentUserUid(),
                photoUrl,
                caption,
            //    getCurrentLocation(),
                getCurrentDate()
            )
        )
    }

    private fun getCurrentDate():Timestamp{
       return Timestamp.now()
    }

   // private fun getCurrentLocation():String{
     //   //todo locationu da tut
    //}
}