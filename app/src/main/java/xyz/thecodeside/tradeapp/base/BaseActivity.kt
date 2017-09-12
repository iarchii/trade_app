package xyz.thecodeside.tradeapp.base

import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import xyz.thecodeside.tradeapp.R

open class BaseActivity : AppCompatActivity(){
    private var snackBar :Snackbar? = null


    protected fun hideOfflineMessage(){
        snackBar?.dismiss()
    }

    protected fun showOfflineMessage(rootView: View){
        snackBar = Snackbar.make(rootView, R.string.app_wont_work_offline,Snackbar.LENGTH_INDEFINITE)
        snackBar?.show()
    }

    override fun onDestroy() {
        hideOfflineMessage()
        super.onDestroy()
    }




}