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
