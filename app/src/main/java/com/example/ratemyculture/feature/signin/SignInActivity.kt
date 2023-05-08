package com.example.ratemyculture.feature.signin

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ratemyculture.BR
import com.example.ratemyculture.R
import com.example.ratemyculture.core.base.BaseActivity
import com.example.ratemyculture.core.base.BaseNavigator
import com.example.ratemyculture.databinding.ActivitySignInBinding
import com.example.ratemyculture.util.openAppSystemSettings
import com.google.firebase.FirebaseApp


class SignInActivity : BaseActivity<ActivitySignInBinding, SignInVM>(), BaseNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_sign_in
    override val viewModel: SignInVM = SignInVM()
    private var mBinding: ActivitySignInBinding? = null

    private val RC_SIGN_IN = 123

    private val PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = super.viewDataBinding
        viewModel.navigator = this
        viewModel.init()
        FirebaseApp.initializeApp(this);
     //   verifyPermissions()
    }

    fun signInWithGoogle(view: View) {
        val signInIntent = viewModel.mGoogleSignInClient.signInIntent
        openActivityForResult(signInIntent, RC_SIGN_IN, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            viewModel.handleGoogleSignInResult(data)
        }
    }

         private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
                permissions ->
                val granted = permissions.all { it.value }
                if (granted) {
                    // Permission granted, do something here
                } else{
                    showAlert(
                        "",
                        "Please go to settings and enable permissions",
                        "OK"
                    ) { _, _ ->
                        openAppSystemSettings()
                    }
                }
         }


    private fun verifyPermissions(){
        val hasPermission = PERMISSIONS.all {
            ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (hasPermission) {
            requestPermissionLauncher.launch(PERMISSIONS)
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
    /*
    // onDestroy metodu, gereksiz nesnelerin temizlenmesi
    override fun onDestroy() {
        super.onDestroy()
    // todo bu kod yuzunden kullanıcı datalarını kaybediyordun viewModel.cleanUp()
    }
 */
}