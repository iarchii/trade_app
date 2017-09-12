package xyz.thecodeside.tradeapp.model

import com.google.gson.annotations.SerializedName

enum class SocketType {
    @SerializedName("connect.connected")
    CONNECT_CONNECTED,
    @SerializedName("connect.failed")
    CONNECT_FAILED,
    @SerializedName("portfolio.performance")
    PORTFOLIO_PERFORMANCE,


}