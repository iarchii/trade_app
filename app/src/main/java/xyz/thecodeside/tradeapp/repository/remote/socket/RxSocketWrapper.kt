package xyz.thecodeside.tradeapp.repository.remote.socket

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import xyz.thecodeside.tradeapp.helpers.Logger
import java.net.Socket
import java.util.concurrent.TimeUnit

class RxSocketWrapper(private val logger: Logger) {

    private var socket: Socket? = null
    private var status: Status =Status.DISCONNECTED
    private val gson =  Gson()

    private val stateFlowable = PublishProcessor.create<Status>().toSerialized()
    private val messageFlowable = PublishProcessor.create<String>().toSerialized()
    private val authStatusFlowable = PublishProcessor.create<AuthStatus>().toSerialized()

    override fun connect(address: String): Flowable<Status> {
        return Flowable
                .fromCallable {
                    //reuse existing connection, only really connect if DISCONNECTED
                    if (status == Status.DISCONNECTED) {
                        connectSocket(address)
                    }
                }
                .flatMap { stateFlowable }
    }

    override fun observeSocketMessages(): Flowable<String> = messageFlowable

    override fun disconnect() {
        /* wait a second before disconnecting the socket, because when a message is sent just before disconnecting the socket,
         * the socketcluster doesn't send it - it disconnect before it's sent. This is a big problem when resigning from game. */
        Single.timer(1, TimeUnit.SECONDS)
                .subscribe({ socket?.disconnect() }, {})
    }

    override fun send(message: String) {
        Log.i("Socket    SEND", message)
        socket?.emit("msg", message)
    }

    override fun authorise(token: String): Single<AuthStatus> =
            Single.fromCallable { socket?.emit("login", token) }
                    .flatMap({authStatusFlowable.firstOrError()})

    private fun connectSocket(address: String) {
        val socket = Socket(address)
        socket.setListener(socketClusterListener)
        socket.disableLogging()
        socket.connect()
    }

    val socketClusterListener = object : BasicListener {

        private var  authoriserDisposable: Disposable? = null

        override fun onConnected(newSocket: io.github.sac.Socket, headers: MutableMap<String, MutableList<String>>) {
            socket = newSocket
            setStatus(Status.CONNECTED)
            registerAuthStatusListener(newSocket)
        }

        override fun onConnectError(websocket: io.github.sac.Socket, exception: WebSocketException) = reconnect(websocket)

        override fun onDisconnected(websocket: io.github.sac.Socket?, serverCloseFrame: WebSocketFrame?, clientCloseFrame: WebSocketFrame?, closedByServer: Boolean) {
            when {
                clientCloseFrame?.closeCode!=WebSocketCloseCode.NORMAL -> reconnect(websocket)
                else -> clear()
            }
        }

        private fun reconnect(websocket: Socket?) {
            Single.timer(500, TimeUnit.MILLISECONDS)
                    .subscribe({ websocket?.connect() }, {})
        }

        override fun onAuthentication(socket: io.github.sac.Socket, status: Boolean) {
            authoriserDisposable = socketAuthoriser
                    .authorise(this@RxSocketClusterWrapperImpl)
                    .subscribe(
                            { setStatus(Status.READY) },
                            { handleError(it) }
                    )
        }

        override fun onSetAuthToken(token: String, socket: io.github.sac.Socket) {
            Log.i("socket","onSetAuthToken")
            socket.setAuthToken(token)
        }

        private fun handleError(throwable: Throwable) {
            logger.logException(throwable)
            clear()
        }

        private fun clear() {
            authoriserDisposable?.dispose()
            socket = null
            setStatus(Status.DISCONNECTED)
        }

    }

    private fun setStatus(status: Status){
        this.status = status
        stateFlowable.onNext(status)
    }

    private fun registerAuthStatusListener(socket: Socket) {
        socket.on("loginStatus", { name, data ->
            authStatusFlowable.onNext(gson.adapter(AuthStatus::class.java).fromJson(data.toString()))
        })
    }

    override fun registerUserChannel(channelName: String): Single<Boolean> {
        var emitter: SingleEmitter<Boolean>? = null
        val observable = Single.create<Boolean> { emitter = it }

        if(socket==null) emitter?.onSuccess(false)

        val channel = socket?.createChannel(channelName)
        channel?.subscribe { name, error, data ->
            Log.i("channel subscribed", "$name / $error / $data")
            when(error){
                null -> emitter?.onSuccess(true)
                else -> emitter?.onSuccess(false)
            }
        }
        channel?.onMessage { name, data ->
            Log.i("Socket RECEIVE", "$data")
            messageFlowable.onNext(data.toString())
        }
        return observable
    }
    
    enum class Status {
        DISCONNECTED,
        CONNECTING,
        CONNECTED,
        READY
    }
    

}