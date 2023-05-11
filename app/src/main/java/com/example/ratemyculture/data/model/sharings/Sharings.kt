package com.example.ratemyculture.data.model.sharings

import com.google.firebase.Timestamp

data class Sharings(
    val userId: String,
    val photoUrl: String,
    val caption: String,
  //  val location: String,
    val uploadDate: Timestamp
)
