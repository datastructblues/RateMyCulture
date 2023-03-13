package com.example.ratemyculture.feature.signin

import android.content.Intent
import androidx.databinding.ObservableField
import com.example.ratemyculture.R
import com.example.ratemyculture.core.base.BaseNavigator
import com.example.ratemyculture.core.base.BaseViewModel
import com.example.ratemyculture.feature.signup.SignUpActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInVM : BaseViewModel<BaseNavigator>() {

    var email = ObservableField<String>("")
    var password = ObservableField<String>("")
    private val firebaseAuth by lazy { Firebase.auth }

    fun credentialLogin() {
        val email = email.get()!!
        val password = password.get()!!
        if (email.isEmpty() || password.isEmpty()) {
            navigator?.showAlert(
                getLocalizedString(R.string.error_title),
                getLocalizedString(R.string.error_message_login),
                getLocalizedString(R.string.ok)
            ) { dialog, _ -> dialog.dismiss() }
        }else{
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        navigator?.getContext()
                            ?.let {
                                navigator?.showToast(
                                    it,
                                    getLocalizedString(R.string.sign_in_successful),
                                    false
                                )
                            }
                    } else {
                        navigator?.showAlert(
                            getLocalizedString(R.string.error_message_sign_up),
                            getLocalizedString(R.string.retry),
                            getLocalizedString(R.string.ok)
                        ) { dialog, _ -> dialog.dismiss() }
                    }
                }
        }
    }

     private fun checkIntentData(): Intent? {
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

    //todo forget password

}