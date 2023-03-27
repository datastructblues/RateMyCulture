package com.example.ratemyculture.util

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

val firebaseAuth by lazy { Firebase.auth }
val fbDatabase by lazy { Firebase.firestore }
