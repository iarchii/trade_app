package xyz.thecodeside.tradeapp.model

import com.google.gson.annotations.SerializedName

enum class SocketTopic {
    @SerializedName("connect.connected")
    CONNECTED,
    @SerializedName("connect.failed")
    CONNECTED_FAILED
}