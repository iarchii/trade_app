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

/**
 * This method converts dp unit to equivalent pixels, depending on device density.

 * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
 * *
 * @param context Context to get resources and device specific display metrics
 * *
 * @return A value to represent px equivalent to dp depending on device density
 */
fun View.convertDpToPixel(dp: Int): Int{
    val resources = context.resources
    val metrics = resources.displayMetrics
    val px = dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    return px.toInt()
}

/**
 * This method converts device specific pixels to density independent pixels.

 * @param px A value in px (pixels) unit. Which we need to convert into db
 * *
 * @param context Context to get resources and device specific display metrics
 * *
 * @return A value to represent dp equivalent to px value
 */
fun View.convertPixelsToDp(px: Int): Int {
    val resources = context.resources
    val metrics = resources.displayMetrics
    val dp = px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    return dp.toInt()
}


fun EditText.getStringText(): String = this.text.toString()