package xyz.thecodeside.tradeapp.model

import com.google.gson.annotations.SerializedName

const val SOCKET_TOPIC_NAME = "t"
const val SOCKET_BODY_NAME = "body"

open class BaseSocket(
        @SerializedName(SOCKET_TOPIC_NAME)
        val type: SocketType,
        @SerializedName(SOCKET_BODY_NAME)
        val body: BaseSocketBody
)

