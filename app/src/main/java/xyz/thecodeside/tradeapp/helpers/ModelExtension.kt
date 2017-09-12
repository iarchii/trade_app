package xyz.thecodeside.tradeapp.helpers

import xyz.thecodeside.tradeapp.model.Product


fun Product.calculateDiff() : Float =
        ((this.currentPrice.amount-this.closingPrice.amount)/this.closingPrice.amount) * 100f