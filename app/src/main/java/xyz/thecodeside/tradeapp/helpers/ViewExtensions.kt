package xyz.thecodeside.tradeapp.helpers

import android.util.DisplayMetrics
import android.view.View
import android.widget.EditText


fun View.hide() {
    this.visibility = View.GONE
}
fun View.show() {
    this.visibility = View.VISIBLE
}
fun View.invisible() {
    this.visibility = View.INVISIBLE
}


fun View.convertDpToPixel(dp: Int): Int{
    val resources = context.resources
    val metrics = resources.displayMetrics
    val px = dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    return px.toInt()
}

fun View.convertPixelsToDp(px: Int): Int {
    val resources = context.resources
    val metrics = resources.displayMetrics
    val dp = px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    return dp.toInt()
}


fun EditText.getStringText(): String = this.text.toString()