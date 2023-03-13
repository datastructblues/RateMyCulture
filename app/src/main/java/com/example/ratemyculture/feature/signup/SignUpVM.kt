package com.example.ratemyculture.feature.signup

import android.content.Intent
import androidx.databinding.ObservableField
import com.example.ratemyculture.R
import com.example.ratemyculture.core.base.BaseNavigator
import com.example.ratemyculture.core.base.BaseViewModel
import com.example.ratemyculture.data.authentication.model.User
import com.example.ratemyculture.feature.signin.SignInActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpVM : BaseViewModel<BaseNavigator>() {

    private val firebaseAuth by lazy { Firebase.auth }
    private val fbDatabase by lazy { Firebase.firestore }

    var username = ObservableField<String>("")
    var email = ObservableField<String>("")
    var password = ObservableField<String>("")
    var passwordConfirm = ObservableField<String>("")


    fun signUp() {
        val username = username.get()!!
        val email = email.get()!!
        val password = password.get()!!
        if (email.isEmpty() || password.isEmpty()) {
            navigator?.showAlert(
                getLocalizedString(R.string.error_title),
                getLocalizedString(R.string.error_message_login),
                getLocalizedString(R.string.ok)
            ) { dialog, _ -> dialog.dismiss() }
        } else {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        navigator?.getContext()
                            ?.let {
                                navigator?.showToast(
                                    it,
                                    getLocalizedString(R.string.sign_up_successful),
                                    false
                                )
                            }
                        fbDatabase.collection("users")
                            .document(firebaseAuth.currentUser?.uid.toString())
                            .set(User(username, email, password))

                        navigator?.openActivity(
                            Intent(
                                navigator?.getContext(),
                                SignInActivity::class.java
                            ), true
                        )
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
}