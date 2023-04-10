package com.example.ratemyculture.feature.signin

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import androidx.databinding.ObservableField
import com.example.ratemyculture.R
import com.example.ratemyculture.core.base.BaseNavigator
import com.example.ratemyculture.core.base.BaseViewModel
import com.example.ratemyculture.data.authentication.model.GoogleUser
import com.example.ratemyculture.feature.main.MainActivity
import com.example.ratemyculture.feature.signup.SignUpActivity
import com.example.ratemyculture.util.fbDatabase
import com.example.ratemyculture.util.firebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SignInVM : BaseViewModel<BaseNavigator>() {

    var email = ObservableField<String>("")
    var password = ObservableField<String>("")

    lateinit var mGoogleSignInClient: GoogleSignInClient

    fun credentialLogin() {
        val email = email.get()!!
        val password = password.get()!!
        if (email.isEmpty() || password.isEmpty()) {
            navigator?.showAlert(
                getLocalizedString(R.string.error_title),
                getLocalizedString(R.string.error_message_login),
                getLocalizedString(R.string.ok)
            ) { dialog, _ -> dialog.dismiss() }
        } else {
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

    fun startSignUpActivity() {
        println("SignInActivity.startSignUpActivity")
        checkIntentData()?.let {
            navigator?.openActivity(it, false)
        }
    }

    //todo forget password

    fun handleGoogleSignInResult(data: Intent?) {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
            firebaseAuthWithGoogle(credential)

        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, "Google sign in failed", e)
            // hata durumunda kullanıcıya bilgi verilebilir
        }
    }


    private fun firebaseAuthWithGoogle(credential: AuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result?.additionalUserInfo?.isNewUser == true) {
                        fbDatabase.collection("googleUsers")
                            .document(firebaseAuth.currentUser?.uid.toString())
                            .set(
                                GoogleUser(
                                    firebaseAuth.currentUser?.email.toString(),
                                    firebaseAuth.currentUser?.displayName.toString(),
                                    0,
                                    firebaseAuth.currentUser?.photoUrl.toString()
                                )
                            )
                        Log.d(TAG, "firebaseAuthWithGoogle: " + firebaseAuth.currentUser?.uid.toString())
                    }
                    val user = FirebaseAuth.getInstance().currentUser
                    openMainActivity()
                    Log.d(TAG, "onActivityResult: " + user.toString())
                } else {
                    navigator?.showAlert(
                        getLocalizedString(R.string.error_message_login),
                        getLocalizedString(R.string.retry),
                        getLocalizedString(R.string.ok)
                    ) { dialog, _ -> dialog.dismiss() }
                    Log.d(TAG, "onActivityResult: " + task.exception.toString())
                }
            }
    }
//google login
    fun init() {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getLocalizedString(R.string.web_client_id))
                .requestEmail()
                .build()

        mGoogleSignInClient =
            navigator?.getContext()
                ?.let { GoogleSignIn.getClient(it, gso) }!!
    Log.d(TAG, "init: $mGoogleSignInClient")
    }
//signout
    fun cleanUp() {
        mGoogleSignInClient.signOut()
        firebaseAuth.signOut()
    }

    private fun openMainActivity() {
        val intent = Intent(navigator?.getContext(), MainActivity::class.java)
        navigator?.openActivity(intent, true)
    }
}
