package xyz.thecodeside.tradeapp.model


data class TradingQuote(
		var securityId: String,// {productId}
		var currentPrice: Float// 10692.3
) :BaseSocketBody