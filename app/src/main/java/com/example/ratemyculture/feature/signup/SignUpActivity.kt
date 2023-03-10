package com.example.ratemyculture.feature.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.ratemyculture.R
import com.example.ratemyculture.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private val viewModel: SignUpVM by viewModels()
    private lateinit var mBinding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        mBinding.viewModel = viewModel
    }
}