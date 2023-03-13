package com.example.ratemyculture.core.base

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.fragment.app.Fragment

interface BaseNavigator {
    fun finishActivity()
    fun finishAndRemoveTask()
    fun showLoading()
    fun hideLoading()
    fun onBackPressed()
    fun openActivity(intent: Intent, clear: Boolean)
    fun openFragment(fragment: Fragment)
    fun popFragment(depth: Int)
    fun getContext(): Context?
    fun getLocalizedString(id: Int): String
    fun setActionBarTitle(title: String)
    fun showAlert(title: String, message: String, button: String, successListener: DialogInterface.OnClickListener)
    fun showAlert(
        title: String, message: String, buttonOne: String, buttonOneListener: DialogInterface.OnClickListener?,
        buttonTwo: String?, buttonTwoListener: DialogInterface.OnClickListener?,
        buttonThree: String?, buttonThreeListener: DialogInterface.OnClickListener?
    )
    fun showToast(context: Context, message: String, isLengthLong: Boolean)
}