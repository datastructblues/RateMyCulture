package com.example.ratemyculture.data.model.sharings

import com.google.firebase.Timestamp

data class Sharing(
    val userId: String = "",
    val photoUrl: String = "",
    val caption: String = "",
  //  val location: String,
    val uploadDate: Timestamp = Timestamp.now(),
)
