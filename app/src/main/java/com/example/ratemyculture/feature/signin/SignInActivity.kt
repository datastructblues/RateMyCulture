package com.example.ratemyculture.feature.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.ratemyculture.BR
import com.example.ratemyculture.R
import com.example.ratemyculture.core.base.BaseActivity
import com.example.ratemyculture.core.base.BaseNavigator
import com.example.ratemyculture.databinding.ActivitySignInBinding


class SignInActivity : BaseActivity<ActivitySignInBinding, SignInVM>(), BaseNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_sign_in
    override val viewModel: SignInVM = SignInVM()
    private var mBinding: ActivitySignInBinding? = null

    private val RC_SIGN_IN = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = super.viewDataBinding
        viewModel.navigator = this
        viewModel.init()
    }

    fun signInWithGoogle(view:View){
        val signInIntent = viewModel.mGoogleSignInClient.signInIntent
        openActivityForResult(signInIntent, RC_SIGN_IN, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            viewModel.handleGoogleSignInResult(data)
        }
    }

    // onStart metodu, kullanıcı oturum açmış mı kontrolü yapılır

    /*
    override fun onStart() {
        super.onStart()
        val currentUser = viewModel.firebaseAuth.currentUser
        if (currentUser != null) {
            // Kullanıcı oturum açmış, profil sayfasına yönlendirilir
           // openActivity(Intent(this,ProfileActivity::class.java), true)
        } else {
            // Kullanıcı oturum açmamış, giriş yapması istenir
        }
    }

     */

    // onDestroy metodu, gereksiz nesnelerin temizlenmesi
    override fun onDestroy() {
        super.onDestroy()
        viewModel.cleanUp()
    }
}