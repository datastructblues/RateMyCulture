package com.example.ratemyculture.feature.signin

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.ratemyculture.feature.signup.SignUpActivity

class SignInVM : ViewModel() {

    var email = ObservableField<String>("")
    var password = ObservableField<String>("")


    fun credentialLogin(){

    }


}