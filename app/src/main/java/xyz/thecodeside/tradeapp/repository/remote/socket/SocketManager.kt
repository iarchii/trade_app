package xyz.thecodeside.tradeapp.repository.remote.socket

import io.reactivex.Completable
import io.reactivex.Flowable
import xyz.thecodeside.tradeapp.helpers.Logger
import xyz.thecodeside.tradeapp.model.BaseSocket
import xyz.thecodeside.tradeapp.model.SocketRequest
import java.util.concurrent.TimeUnit

class SocketManager(private val socketAddress: String,
                    private val socket: RxSocketWrapper,
                    private val packer: SocketItemPacker,
                    private val token: String,
                    private val logger: Logger) {

    private val TIMEOUT_SECONDS = 3L

    fun connect(): Completable = socket
            .connect(socketAddress, token)
            .filter { it == RxSocketWrapper.Status.READY }
            .timeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .firstOrError()
            .toCompletable()
            .doOnError {
                disconnect()
            }


    fun disconnect() = socket.disconnect()

    fun send(item: SocketRequest) {
        val token = this.token
        socket.send(packer.pack(item))
    }

    fun observe(): Flowable<BaseSocket> = socket
            .observeSocketMessages()
            .flatMap { unpackItem(it) }

    @Suppress("UNCHECKED_CAST")
    fun <T : BaseSocket> observe(clazz: Class<T>): Flowable<T> = observe()
            .filter { it.javaClass == clazz }
            .map {
                it as T
            }

    fun unpackItem(envelope: String?): Flowable<BaseSocket> = Flowable
            .fromCallable { (packer.unpack(envelope)) }
            .onErrorResumeNext { e: Throwable ->
                logger.logException(e)
                Flowable.never<BaseSocket>()
            }


}

