package xyz.thecodeside.tradeapp.model

import com.google.gson.annotations.SerializedName

const val SOCKET_TOPIC_NAME = "t"
const val SOCKET_BODY_NAME = "t"

open class BaseSocket(
        @SerializedName(SOCKET_TOPIC_NAME)
        val topic: SocketTopic,
        @SerializedName(SOCKET_BODY_NAME)
        val body: BaseSocketBody
)

