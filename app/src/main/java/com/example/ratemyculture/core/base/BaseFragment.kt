package com.example.ratemyculture.core.base

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

abstract class BaseFragment<T: ViewDataBinding, V: BaseFragmentViewModel<*>, K:BaseDelegate>: Fragment(), BaseNavigator {
    var mDelegate: WeakReference<K?> = WeakReference(null)

    val delegate : K?
        get() = mDelegate.get()

    var activity: WeakReference<AppCompatActivity?> = WeakReference(null)
        private set

    var baseActivity: WeakReference<BaseActivity<*, *>?> = WeakReference(null)
        private set

    private var mRootView: View? = null
    var viewDataBinding: T? = null
        private set

    protected lateinit var mViewModel: V
    private var fragmentTag: String? = null



//    open var TITLE:String = ""
    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract val bindingVariable: Int

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract val viewModel: V

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            val activity = context as BaseActivity<*, *>?
            this.baseActivity = WeakReference(activity)
        }

        //This code usage from outside
        if (context is AppCompatActivity) {
            val activity = context as AppCompatActivity
            this.activity = WeakReference(activity)
        }

        if (context is BaseDelegate) {
            mDelegate = WeakReference(context as K)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        mViewModel = viewModel
        viewModel.onCreate()
        setHasOptionsMenu(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mRootView = viewDataBinding!!.root
        return mRootView
    }

    override fun onDetach() {
        baseActivity = WeakReference(null)
        activity = WeakReference(null)
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding!!.setVariable(bindingVariable, mViewModel)
        viewDataBinding!!.executePendingBindings()
    }

    fun getFragmentTag(): String? {
        return fragmentTag
    }

    fun setFragmentTag(fragmentTag: String?) {
        this.fragmentTag = fragmentTag
    }

    fun hideKeyboard() {
        delegate?.hideKeyboard()
    }

    override fun setActionBarTitle(title: String) {
        delegate?.setActionBarTitle(title)
    }

    override fun showLoading() {
        delegate?.showLoading()
    }

    override fun hideLoading() {
        delegate?.hideLoading()
    }

    override fun showAlert(
        title: String,
        message: String,
        button: String,
        successListener: DialogInterface.OnClickListener
    ) {
        delegate?.showAlert(title,message,button,successListener)
    }

    override fun showAlert(title:String, message: String, buttonOne: String, buttonOneListener: DialogInterface.OnClickListener?,
                           buttonTwo: String?, buttonTwoListener: DialogInterface.OnClickListener?,
                           buttonThree: String?, buttonThreeListener: DialogInterface.OnClickListener?){
        delegate?.showAlert(title,message,buttonOne,buttonOneListener,buttonTwo, buttonTwoListener, buttonThree, buttonThreeListener)
    }

    override fun showToast(context: Context, message: String,isLengthLong:Boolean) {
        if (isLengthLong)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        else
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        delegate?.onBackPressed()
    }

    override fun openFragment(fragment:Fragment) {
//        mFragmentNavigation?.pushFragment(fragment)
    }

    override fun popFragment(depth: Int) {
//        mFragmentNavigation?.popFragment(depth)
    }

    override fun openActivity(intent: Intent, clear:Boolean) {
        delegate?.openActivity(intent,clear) ?: activity.get()?.startActivity(intent)
    }

    override fun getLocalizedString(id: Int): String {
        return context?.getString(id) ?: ""
    }

    override fun finishActivity() {
        delegate?.finishActivity()
    }

    override fun finishAndRemoveTask() {
        delegate?.finishAndRemoveTask()
    }

}