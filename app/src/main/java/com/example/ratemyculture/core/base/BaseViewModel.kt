package com.example.ratemyculture.core.base


import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import java.lang.ref.WeakReference

abstract class BaseViewModel<N: BaseNavigator> {
    var isLoading = ObservableBoolean(false)
    var toolbarEnable: ObservableBoolean = ObservableBoolean(true)
    var toolbarTitle: ObservableField<String> = ObservableField("")
    var toolbarLogo: ObservableField<Int> = ObservableField(0)
    var rightButtonEnable: ObservableBoolean = ObservableBoolean(false)
    var mNavigator: WeakReference<N>? = null

    var navigator: N?
        get() = mNavigator?.get()
        set(navigator) {
            this.mNavigator = WeakReference(navigator)
        }

    fun onBackButtonClicked() {
        navigator?.onBackPressed()
    }

    fun getLocalizedString(id: Int) : String {
        return navigator?.getLocalizedString(id) ?: id.toString()
    }

    open fun onResume() {

    }

    open fun onPause() {

    }

    open fun onRightButtonClicked() {

    }
}