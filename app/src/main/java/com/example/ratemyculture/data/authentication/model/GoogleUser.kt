package com.example.ratemyculture.data.authentication.model

data class GoogleUser(
    val email:String,
    val username:String,
    val point:Int,
    val photo_url: String?
)
