package xyz.thecodeside.tradeapp.model

import android.support.annotation.Keep
import java.io.Serializable

@Keep
data class Product(
        val symbol: String,// FRANCE40
        val securityId: String,// 26608
        val displayName: String,// French Exchange
        val currentPrice: Price,
        val closingPrice: Price,
        val productMarketStatus: MarketStatus
) : Serializable

@Keep
enum class MarketStatus {
    OPEN,
    CLOSED
}

@Keep
data class Price(
        val currency: String,// EUR
        val decimals: Int,// 1
		var amount: Float// 4371.8
) : Serializable

