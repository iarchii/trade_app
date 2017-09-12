package xyz.thecodeside.tradeapp.model

import com.google.gson.annotations.SerializedName


class SocketRequest(
		subscribeProduct: List<SubscriptionProduct>? = null,
		unsubscribeProduct: List<SubscriptionProduct>? = null
){
    @SerializedName("subscribeTo")
    val subscribeTo = subscribeProduct?.map { it.subscription }

    @SerializedName("unsubscribeFrom\":")
    val unsubscribeFrom = unsubscribeProduct?.map { it.subscription }
}

class SubscriptionProduct(productId : String) {
    val subscription = "trading.product.$productId"
}
