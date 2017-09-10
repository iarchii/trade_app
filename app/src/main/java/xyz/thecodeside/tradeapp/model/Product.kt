package xyz.thecodeside.tradeapp.model

import android.support.annotation.Keep

@Keep
data class Product(
		var symbol: String,// FRANCE40
		var securityId: String,// 26608
		var displayName: String,// French Exchange
		var currentPrice: CurrentPrice,
		var closingPrice: ClosingPrice
)

@Keep
data class CurrentPrice(
		var currency: String,// EUR
		var decimals: Int,// 1
		var amount: String// 4371.8
)

@Keep
data class ClosingPrice(
		var currency: String,// EUR
		var decimals: Int,// 1
		var amount: String// 4216.4
)