package com.example.ratemyculture.feature.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.ratemyculture.R
import com.example.ratemyculture.databinding.ActivitySignInBinding
import com.example.ratemyculture.feature.signup.SignUpActivity

class SignInActivity : AppCompatActivity() {

    private  val viewModel: SignInVM by viewModels()
    private lateinit var mBinding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        mBinding.viewModel = viewModel

    }


    private fun checkIntentData(): Intent {
        return Intent(this@SignInActivity, SignUpActivity::class.java)
    }

    fun startSignUpActivity(view:View){
        println("SignInActivity.startSignUpActivity")
        checkIntentData().let {
            startActivity(it)
            finish()
        }
    }
}