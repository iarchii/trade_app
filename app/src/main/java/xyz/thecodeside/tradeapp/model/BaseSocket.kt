package xyz.thecodeside.tradeapp.model

import com.google.gson.annotations.SerializedName

const val TOPIC_NAME = "t"

data class BaseSocket(
        @SerializedName(TOPIC_NAME)
        val topic: SocketTopic,
        val socketBody: String
)

