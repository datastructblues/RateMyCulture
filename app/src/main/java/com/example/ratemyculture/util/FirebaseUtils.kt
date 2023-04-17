package com.example.ratemyculture.util

import android.annotation.SuppressLint
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

val firebaseAuth =Firebase.auth
@SuppressLint("StaticFieldLeak")
val fbDatabase = Firebase.firestore
var fbStorage = Firebase.storage("gs://ratemyculture-81b86.appspot.com/")

fun getDocument(collectionName:String,documentName:String) = fbDatabase.collection(collectionName).document(documentName)

fun getCollection(collectionName:String) = fbDatabase.collection(collectionName)

fun getStorageReference(storagePath:String) = fbStorage.reference.child(storagePath)

fun getStorageReference() = fbStorage.reference

fun getAuth() = firebaseAuth

fun getAuthCurrentUser() = firebaseAuth.currentUser

fun getAuthCurrentUserUid() = firebaseAuth.currentUser?.uid.toString()

fun getAuthCurrentUserEmail() = firebaseAuth.currentUser?.email.toString()

fun getAuthCurrentUserDisplayName() = firebaseAuth.currentUser?.displayName.toString()

fun getAuthCurrentUserPhotoUrl() = firebaseAuth.currentUser?.photoUrl.toString()

fun getAuthCurrentUserPhoneNumber() = firebaseAuth.currentUser?.phoneNumber.toString()

fun getAuthCurrentUserProviderId() = firebaseAuth.currentUser?.providerId.toString()

fun getAuthCurrentUserIsEmailVerified() = firebaseAuth.currentUser?.isEmailVerified.toString()

fun getAuthCurrentUserMetadata() = firebaseAuth.currentUser?.metadata.toString()

fun getAuthCurrentUserTenantId() = firebaseAuth.currentUser?.tenantId.toString()

fun getAuthCurrentUserMultiFactor() = firebaseAuth.currentUser?.multiFactor.toString()

fun getAuthCurrentUserMultiFactorInfo() = firebaseAuth.currentUser?.multiFactor?.enrolledFactors.toString()


