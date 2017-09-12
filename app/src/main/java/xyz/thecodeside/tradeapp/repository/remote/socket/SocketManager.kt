package xyz.thecodeside.tradeapp.repository.remote.socket

import io.reactivex.Flowable
import xyz.thecodeside.tradeapp.helpers.Logger
import xyz.thecodeside.tradeapp.model.BaseSocket
import xyz.thecodeside.tradeapp.model.SocketRequest
import xyz.thecodeside.tradeapp.model.SocketType

class SocketManager(private val socketAddress: String,
                    private val socket: RxSocketWrapper,
                    private val packer: SocketItemPacker,
                    private val logger: Logger) {

    companion object {
        const val TAG = "SOCKET"
    }

    fun connect(): Flowable<RxSocketWrapper.Status> = socket
            .connect(socketAddress)
            .doOnError {
                disconnect()
            }

    private fun isReady(it: RxSocketWrapper.Status) =
            it == RxSocketWrapper.Status.READY


    fun disconnect() = socket.disconnect()

    fun send(item: SocketRequest) {
        socket.send(packer.pack(item))
    }

    fun observe(): Flowable<BaseSocket> = socket
            .observeSocketMessages()
            .flatMap { unpackItem(it) }
            .map {
                checkIsReady(it)
                it
            }

    private fun checkIsReady(it: BaseSocket) {
        if (isConnected() && isConnectedMessage(it)) {
            socket.status = RxSocketWrapper.Status.READY
        }
    }

    private fun isConnectedMessage(it: BaseSocket) =
            it.type == SocketType.CONNECT_CONNECTED

    private fun isConnected() = socket.status == RxSocketWrapper.Status.CONNECTED

    fun unpackItem(envelope: String?): Flowable<BaseSocket> = Flowable
            .fromCallable { (packer.unpack(envelope)) }
            .onErrorResumeNext { e: Throwable ->
                logger.logException(e)
                Flowable.never<BaseSocket>()
            }


}

