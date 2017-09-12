package xyz.thecodeside.tradeapp.helpers

import xyz.thecodeside.tradeapp.model.MarketStatus
import xyz.thecodeside.tradeapp.model.Price
import xyz.thecodeside.tradeapp.model.Product


val mockProdID = "some-mock-id"

val mockCurrentPrcie = Price("EUR", 1, 1.2f)
val mockProdDetails = Product(
        "FRANCE40",
        "26608",
        "French Exchange",
        mockCurrentPrcie,
        mockCurrentPrcie,
        MarketStatus.OPEN
)