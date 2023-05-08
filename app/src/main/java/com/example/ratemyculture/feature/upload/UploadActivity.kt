package com.example.ratemyculture.feature.upload

import android.os.Bundle
import com.example.ratemyculture.BR
import com.example.ratemyculture.R
import com.example.ratemyculture.core.base.BaseActivity
import com.example.ratemyculture.core.base.BaseNavigator
import com.example.ratemyculture.databinding.ActivityUploadBinding

class UploadActivity : BaseActivity<ActivityUploadBinding,UploadVM>(),BaseNavigator{
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_upload
    override val viewModel: UploadVM = UploadVM()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        viewModel.navigator = this
    }

}