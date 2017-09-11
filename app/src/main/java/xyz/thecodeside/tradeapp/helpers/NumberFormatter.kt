package xyz.thecodeside.tradeapp.helpers

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


object NumberFormatter{
    private val percentFormatter by lazy {
        val df = NumberFormat.getNumberInstance(Locale.getDefault()) as DecimalFormat
        df.maximumFractionDigits = 2
        df.minimumFractionDigits = 2
        df.negativePrefix = "- "
        df.positivePrefix = "+ "
        df.negativeSuffix = "%"
        df.positiveSuffix = "%"
        df
    }

    private val normalFormatter by lazy {
        NumberFormat.getNumberInstance(Locale.getDefault()) as DecimalFormat
    }

    fun formatPercent(value: Float) = percentFormatter.format(value)
    fun format(value: Float) = normalFormatter.format(value)

}