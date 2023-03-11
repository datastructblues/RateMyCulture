package com.example.ratemyculture.feature.signin

import android.content.DialogInterface
import android.content.Intent
import android.view.View
import androidx.databinding.ObservableField
import com.example.ratemyculture.core.base.BaseNavigator
import com.example.ratemyculture.core.base.BaseViewModel
import com.example.ratemyculture.feature.signup.SignUpActivity

class SignInVM : BaseViewModel<BaseNavigator>() {

    var email = ObservableField<String>("")
    var password = ObservableField<String>("")

    fun credentialLogin() {
        println(email.get())
        val email = email.get()!!
        val password = password.get()!!
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            navigator?.showAlert(
                "Error",
                "Please enter email and password",
                "OK"
            ) { dialog, _ -> dialog.dismiss() }
        }
    }

     fun checkIntentData(): Intent? {
        navigator?.getContext()?.let {
            return Intent(it, SignUpActivity::class.java)
        }
        return null
    }

    fun startSignUpActivity(){
        println("SignInActivity.startSignUpActivity")
        checkIntentData()?.let {
            navigator?.openActivity(it, false)
        }
    }
}