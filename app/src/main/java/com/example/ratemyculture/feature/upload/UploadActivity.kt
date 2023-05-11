package com.example.ratemyculture.feature.upload

import android.net.Uri
import android.os.Bundle
import androidx.core.content.FileProvider
import com.example.ratemyculture.BR
import com.example.ratemyculture.R
import com.example.ratemyculture.core.base.BaseActivity
import com.example.ratemyculture.core.base.BaseNavigator
import com.example.ratemyculture.databinding.ActivityUploadBinding
import java.io.File

class UploadActivity : BaseActivity<ActivityUploadBinding, UploadVM>(), BaseNavigator {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_upload
    override val viewModel: UploadVM = UploadVM()
    private var mBinding: ActivityUploadBinding? = null
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = super.viewDataBinding
        viewModel.navigator = this
        getIntentDataFromCamera()
        uploadImageToStorage()
    }

    private fun getIntentDataFromCamera() {
        //get camera data from MapsFragment
        val intent = intent
        val value = intent.getSerializableExtra("imageFile") as File
        setUploadedImageData(value)
    }

    private fun setUploadedImageData(value: File) {
        val view = mBinding?.uploadedImage
        uri = FileProvider.getUriForFile(this, "com.example.ratemyculture.fileprovider", value)
        view?.setImageURI(uri)
    }

    private fun uploadImageToStorage() {
        val uploadButton = mBinding?.upload
        uploadButton?.setOnClickListener {
            val caption = mBinding?.caption?.text.toString()
            viewModel.uploadSharing(uri,caption)
            finish()
        }
    }
}