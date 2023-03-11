package com.example.ratemyculture.feature.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.ratemyculture.BR
import com.example.ratemyculture.R
import com.example.ratemyculture.core.base.BaseActivity
import com.example.ratemyculture.core.base.BaseNavigator
import com.example.ratemyculture.databinding.ActivitySignInBinding
import com.example.ratemyculture.feature.signup.SignUpActivity

class SignInActivity : BaseActivity<ActivitySignInBinding, SignInVM>(), BaseNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_sign_in
    override val viewModel: SignInVM = SignInVM()
    private var mBinding: ActivitySignInBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = super.viewDataBinding
        viewModel.navigator = this

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