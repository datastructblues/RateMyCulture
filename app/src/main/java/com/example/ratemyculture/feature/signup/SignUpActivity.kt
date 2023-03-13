package com.example.ratemyculture.feature.signup

import android.os.Bundle
import com.example.ratemyculture.BR
import com.example.ratemyculture.R
import com.example.ratemyculture.core.base.BaseActivity
import com.example.ratemyculture.core.base.BaseNavigator
import com.example.ratemyculture.databinding.ActivitySignUpBinding

class SignUpActivity : BaseActivity<ActivitySignUpBinding, SignUpVM>(), BaseNavigator {

    private var mBinding: ActivitySignUpBinding? = null

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_sign_up
    override val viewModel: SignUpVM = SignUpVM()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = super.viewDataBinding
        viewModel.navigator = this
    }
}