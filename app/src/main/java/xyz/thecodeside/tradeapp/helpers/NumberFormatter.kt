package xyz.thecodeside.tradeapp.helpers

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


object NumberFormatter{
    private val DEFAULT_DECIMALS = 2

    private val percentFormatter by lazy {
        val df = NumberFormat.getNumberInstance(Locale.getDefault()) as DecimalFormat
        df.maximumFractionDigits = DEFAULT_DECIMALS
        df.minimumFractionDigits = DEFAULT_DECIMALS
        df.negativePrefix = "- "
        df.positivePrefix = "+ "
        df.negativeSuffix = "%"
        df.positiveSuffix = "%"
        df
    }

    fun formatPercent(value: Float) = percentFormatter.format(value)

    fun format(value: Float, decimals : Int = DEFAULT_DECIMALS): String? {
        val normalFormatter =
                NumberFormat.getNumberInstance(Locale.getDefault()) as DecimalFormat
        normalFormatter.maximumFractionDigits = decimals
        normalFormatter.minimumFractionDigits = decimals
        return normalFormatter.format(value)
    }

}