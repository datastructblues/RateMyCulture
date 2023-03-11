package com.example.ratemyculture.core.base


import android.content.Context
import android.content.DialogInterface
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import java.lang.ref.WeakReference

abstract class BaseFragmentViewModel<N: BaseNavigator>: ViewModel() {
    var mNavigator: WeakReference<N>? = null

    var navigator: N?
        get() = mNavigator?.get()
        set(navigator) {
            this.mNavigator = WeakReference(navigator)
        }

    fun openFragment(fragment: Fragment) {
        if (navigator is BaseNavigator)
            (navigator as BaseNavigator)?.openFragment(fragment)
    }

    open fun showAlert(
        title: String,
        message: String,
        button: String,
        successListener: DialogInterface.OnClickListener
    ) {
        if (navigator is BaseNavigator) {
            (navigator as BaseNavigator).showAlert(title, message, button, successListener)
        }
    }

    fun getContext(): Context? {
        if (navigator is BaseNavigator) {
            return (navigator as BaseNavigator).getContext()
        }
        throw Exception("Use Base Navigator")
    }


    fun showLoading() {
        if (navigator is BaseNavigator && navigator != null) {
            (navigator as BaseNavigator).showLoading()
        }
    }

    fun hideLoading() {
        if (navigator != null && navigator is BaseNavigator) {
            (navigator as BaseNavigator).hideLoading()
        }
    }

    open fun onPause() {

    }

    open fun onResume() {

    }

    open fun onCreate() {

    }
}