package xyz.thecodeside.tradeapp.model

import android.support.annotation.Keep
import java.io.Serializable

@Keep
data class Product(
		var symbol: String,// FRANCE40
		var securityId: String,// 26608
		var displayName: String,// French Exchange
		var currentPrice: Price,
		var closingPrice: Price
) : Serializable

@Keep
data class Price(
		var currency: String,// EUR
		var decimals: Int,// 1
		var amount: Float// 4371.8
) : Serializable

