package com.example.ratemyculture.core.base


import android.content.DialogInterface
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

interface BaseDelegate {
    fun finishActivity()
    fun finishAndRemoveTask()
    fun showLoading()
    fun hideLoading()
    fun onBackPressed()
    fun hideKeyboard()
    fun openActivity(intent: Intent, clear: Boolean)
    fun setActionBarTitle(title: String)
    fun openActivityForResult(intent: Intent, id: Int, clear: Boolean)
    fun registerForActivity(
        startActivityForResult: ActivityResultContracts.StartActivityForResult,
        activityResultCallback: ActivityResultCallback<ActivityResult>
    ): ActivityResultLauncher<Intent>

    fun showAlert(title: String, message: String, button: String, successListener: DialogInterface.OnClickListener)
    fun showAlert(
        title: String, message: String, buttonOne: String, buttonOneListener: DialogInterface.OnClickListener?,
        buttonTwo: String?, buttonTwoListener: DialogInterface.OnClickListener?,
        buttonThree: String?, buttonThreeListener: DialogInterface.OnClickListener?
    )
}