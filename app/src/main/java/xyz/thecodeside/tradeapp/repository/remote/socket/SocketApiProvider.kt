package xyz.thecodeside.tradeapp.repository.remote.socket

import okhttp3.OkHttpClient
import xyz.thecodeside.tradeapp.helpers.Logger
import xyz.thecodeside.tradeapp.helpers.SOCKET_TOKEN
import xyz.thecodeside.tradeapp.helpers.SOCKET_URL


class SocketApiProvider(private val logger: Logger) {
    fun provide(): SocketManager {
        val client = OkHttpClient()
        return SocketManager(
                SOCKET_URL,
                RxSocketWrapper(client, logger, SOCKET_TOKEN),
                SocketItemPacker(),
                logger
        )
    }

}

