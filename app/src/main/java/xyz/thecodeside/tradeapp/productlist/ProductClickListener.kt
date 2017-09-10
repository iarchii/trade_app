package xyz.thecodeside.tradeapp.productlist

import xyz.thecodeside.tradeapp.model.Product

interface ProductClickListener {
    fun onClick(product: Product)
}