package xyz.thecodeside.tradeapp.model

import android.support.annotation.Keep
import java.io.Serializable

@Keep
data class Product(
		var symbol: String,// FRANCE40
		var securityId: String,// 26608
		var displayName: String,// French Exchange
		var currentPrice: CurrentPrice,
		var closingPrice: ClosingPrice
) : Serializable

@Keep
data class CurrentPrice(
		var currency: String,// EUR
		var decimals: Int,// 1
		var amount: Float// 4371.8
) : Serializable

@Keep
data class ClosingPrice(
		var currency: String,// EUR
		var decimals: Int,// 1
		var amount: Float// 4216.4
) : Serializable