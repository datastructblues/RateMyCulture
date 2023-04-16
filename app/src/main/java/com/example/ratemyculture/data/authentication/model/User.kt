package com.example.ratemyculture.data.authentication.model

data class User(
    val username:String,
    val point:Int,
    val photo_url: String?
)

/*
todo add this to model
 val photo_url: String?,
    val place_visited: Int,
    val last_check_in: String?
 */
