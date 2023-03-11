package com.example.ratemyculture.core.base

import android.annotation.TargetApi
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseActivity<T: ViewDataBinding, V: BaseViewModel<*>> : AppCompatActivity(), BaseDelegate {

    var viewDataBinding: T? = null
        private set

    private var mViewModel: V? = null

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

    var toolbar: Toolbar? = null

    val baseViewModel: V?
        get() {
            return mViewModel
        }

    private var mProgressDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
        setCustomToolbar()
    }

    private fun setCustomToolbar() {
    }

    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        this.mViewModel = if (mViewModel == null) viewModel else mViewModel
        viewDataBinding!!.setVariable(bindingVariable, mViewModel)
        viewDataBinding!!.executePendingBindings()
    }


    override fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun hideLoading() {

    }

    override fun showLoading() {
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
        viewModel.toolbarTitle.set(title)
    }

    override fun finishAndRemoveTask() {
        super.finishAndRemoveTask()
    }

    override fun showAlert(
        title: String,
        message: String,
        button: String,
        successListener: DialogInterface.OnClickListener
    ) {
        this.showAlert(title, message, button, successListener, null, null, null, null)
    }

    override fun showAlert(
        title: String, message: String, buttonOne: String, buttonOneListener: DialogInterface.OnClickListener?,
        buttonTwo: String?, buttonTwoListener: DialogInterface.OnClickListener?,
        buttonThree: String?, buttonThreeListener: DialogInterface.OnClickListener?
    ) {
        this.runOnUiThread {
            run {
                val builder = AlertDialog.Builder(this@BaseActivity)

                // Set the alert dialog title
                builder.setTitle(title)

                // Display a message on alert dialog
                builder.setMessage(message)

                builder.setCancelable(false)

                // Set a positive button and its click listener on alert dialog
                builder.setPositiveButton(buttonOne) { dialog, which ->
                    // Do something when user press the positive button
                    if (buttonOneListener != null) {
                        buttonOneListener.onClick(dialog, which)
                    } else {
                        dialog.dismiss()
                    }
                }

                if (buttonTwo != null) {
                    builder.setNeutralButton(buttonTwo) { dialog, which ->
                        // Do something when user press the positive button
                        buttonTwoListener?.onClick(dialog, which)
                    }
                }

                if (buttonThree != null) {
                    builder.setNegativeButton(buttonThree) { dialog, which ->
                        // Do something when user press the positive button
                        if (buttonTwoListener != null) {
                            buttonThreeListener!!.onClick(dialog, which)
                        }
                    }
                }

                // Finally, make the alert dialog using builder
                val dialog: AlertDialog = builder.create()

                // Display the alert dialog on app interface
                if (!isFinishing) {
                    dialog.show()
                }
            }
        }
    }

    override fun openActivity(intent: Intent, clear:Boolean) {
        startActivity(intent)
        if (clear) finish()
    }

    override fun openActivityForResult(intent: Intent, id:Int, clear:Boolean) {
        startActivityForResult(intent,id)
        if (clear) finish()
    }

    fun getLocalizedString(id: Int): String {
        return resources.getString(id);
    }

    open fun openFragment(fragment: Fragment) {
        TODO("Not yet implemented")
    }

    open fun popFragment(depth: Int) {
        TODO("Not yet implemented")
    }

    open fun getContext(): Context = this

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    /**
     * Hides keyboard on touching outside of EditText
     */
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val view = currentFocus
        val ret = super.dispatchTouchEvent(event)

        if (view is EditText) {
            val w = currentFocus
            val scrcoords = IntArray(2)
            w!!.getLocationOnScreen(scrcoords)
            val x = event.rawX + w.left - scrcoords[0]
            val y = event.rawY + w.top - scrcoords[1]

            if (event.action == MotionEvent.ACTION_UP && (x < w.left || x >= w.right
                        || y < w.top || y > w.bottom)
            ) {
                hideKeyboard()
            }
        }
        return ret
    }


    override fun registerForActivity(
        startActivityForResult: ActivityResultContracts.StartActivityForResult,
        activityResultCallback: ActivityResultCallback<ActivityResult>
    ): ActivityResultLauncher<Intent> {
        return registerForActivityResult(startActivityForResult, activityResultCallback)
    }

    override fun finishActivity() {
        finish()
    }


}