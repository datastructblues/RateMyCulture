package com.example.ratemyculture.data.authentication.model

data class User(
    val email:String,
    val username:String,
    val password:String,
    val point:Int,
    val photo_url: String?
)
