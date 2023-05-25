package com.example.ratemyculture.feature.main.profile

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.ratemyculture.data.model.sharings.Sharing
import com.example.ratemyculture.util.fbDatabase
import com.example.ratemyculture.util.fbStorage
import com.example.ratemyculture.util.getAuthCurrentUserUid
import com.example.ratemyculture.util.getCollection
import com.google.firebase.firestore.Query
import java.io.ByteArrayOutputStream


class ProfileFragmentVM : ViewModel() {

    //livedata for user profile data
    val username = ObservableField<String>()
    val email = ObservableField<String>()
    val point = ObservableField<String>()
    val photoUrl = ObservableField<String>()


    fun getCurrentUserProfileData(uid: String, context: Context) {
        uid.let {
            val userData = fbDatabase.collection("users").document(uid)
            userData.get().addOnSuccessListener { document ->
                if (document != null) {
                    val username = document.get("username").toString()
                    val email = document.get("email").toString()
                    val password = document.get("password").toString()
                    val point = document.get("point").toString()
                    val photoUrl = document.get("photo_url").toString()
                    this.username.set(username)
                    this.email.set(email)
                    this.point.set(point)
                    this.photoUrl.set(photoUrl)

                    Log.d("ProfileFragmentVM", "getCurrentUserProfileData: $photoUrl")


                } else {
                    println("No such document")
                }
            }.addOnFailureListener { exception ->
            }
        }
    }

    private fun updateCurrentUserProfilePicture(uid: String, photoUrl: Uri) {
        val userData = fbDatabase.collection("users").document(uid)
        userData.get().addOnSuccessListener { document ->
            if (document != null) {
                //set document field
                userData.update("photo_url", photoUrl.toString())
            } else {
                println("No such document")
            }
        }.addOnFailureListener { exception ->
        }
    }

    fun uploadUserPictureToStorage(uid: String?, bitmap: Bitmap) {
        /*
        val storageRef = fbStorage.reference
        val profilePictureRef = storageRef.child("profile_pictures/${firebaseAuth.currentUser?.uid}.jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Log.d("ProfileFragmentVM", "uploadUserPictureToStorage: failed")
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            Log.d("ProfileFragmentVM", "uploadUserPictureToStorage: success")
            // ...
        }

         */
        val storageRef = fbStorage.reference
        val profilePictureRef = storageRef.child("profile_pictures/$uid.jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = profilePictureRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
            taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                Log.d("ProfileFragmentVM", "uploadUserPictureToStorage: success")
                Log.d("ProfileFragmentVM", "uploadUserPictureToStorage: $uri")
                Log.d("ProfileFragmentVM", uid.toString())
                if (uid != null) {
                    updateCurrentUserProfilePicture(uid, uri)
                }
            }
        }
    }

    fun getUserSharings(callback: (List<Sharing>) -> Unit): MutableList<Sharing> {
        val userId = getAuthCurrentUserUid()
        val sharingList = mutableListOf<Sharing>()
        val collectionRef = getCollection("sharings")
        val query = collectionRef.whereEqualTo("userId", userId)
            .orderBy("uploadDate", Query.Direction.DESCENDING)
        query.get().addOnSuccessListener { documents ->
            Log.d("ProfileFragmentVM", "getUserSharings: ${documents.size()}")
            for (document in documents) {
                val sharing = document.toObject(Sharing::class.java)
                sharingList.add(sharing)
            }
            callback(sharingList)
        }.addOnFailureListener { exception ->
            // Hata durumunda burada işlem yapabilirsiniz
            println("Firestore sorgusu başarısız oldu: $exception")
        }
        return sharingList
    }
}