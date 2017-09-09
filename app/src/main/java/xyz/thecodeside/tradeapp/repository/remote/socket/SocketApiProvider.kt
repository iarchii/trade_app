package xyz.thecodeside.tradeapp.repository.remote.socket

import xyz.thecodeside.tradeapp.helpers.Logger
import xyz.thecodeside.tradeapp.helpers.SOCKET_TOKEN
import xyz.thecodeside.tradeapp.helpers.SOCKET_URL


class SocketApiProvider(private val logger: Logger) {
    fun provide(): SocketManager  = SocketManager(
            SOCKET_URL,
            RxSocketWrapper(logger),
            SocketItemPacker(),
            SOCKET_TOKEN,
            logger
    )

}

