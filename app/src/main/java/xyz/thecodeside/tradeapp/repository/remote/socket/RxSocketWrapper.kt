package xyz.thecodeside.tradeapp.repository.remote.socket

import android.util.Log
import com.google.gson.Gson
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import xyz.thecodeside.tradeapp.helpers.Logger
import java.net.Socket
import java.util.concurrent.TimeUnit

class RxSocketWrapper(private val logger: Logger
                      ) : WebSocketListener(){

    private val NORMAL_CLOSURE_STATUS = 1000
    private val NORMAL_CLOSURE_REASON = "DISCONNECT"

    private val AUTHORIZATION_KEY = "Authorization"
    private val AUTH_PREFIX = "Bearer "


    private var socket: WebSocket? = null
    private var status: Status = Status.DISCONNECTED
    private val gson = Gson()

    private val stateFlowable = PublishProcessor.create<Status>().toSerialized()
    private val messageFlowable = PublishProcessor.create<String>().toSerialized()

    fun connect(address: String, token: String): Flowable<Status> {
        return Flowable
                .fromCallable {
                    //reuse existing connection, only really connect if DISCONNECTED
                    if (status == Status.DISCONNECTED) {
                        connectSocket(address)
                    }
                }
                .flatMap { stateFlowable }
    }

    fun observeSocketMessages(): Flowable<String> = messageFlowable

    fun disconnect() {
        socket?.close(NORMAL_CLOSURE_STATUS, NORMAL_CLOSURE_REASON)
    }

    fun send(message: String) {
        Log.i("Socket  SEND", message)
        socket?.send(message)
    }

    private fun connectSocket(address: String, token: String){

        val client = OkHttpClient()
        val request = Request.Builder()
                .url(address)
                .addHeader(AUTHORIZATION_KEY, AUTH_PREFIX + token)
                .build()

        socket = client.newWebSocket(request, this)
        client.newWebSocket(request, this)
        client.dispatcher().executorService().shutdown()
    }

    val socketClusterListener = object : BasicListener {

        private var authoriserDisposable: Disposable? = null

        fun onConnected(newSocket: io.github.sac.Socket, headers: MutableMap<String, MutableList<String>>) {
            socket = newSocket
            setStatus(Status.CONNECTED)
            registerAuthStatusListener(newSocket)
        }

        fun onConnectError(websocket: io.github.sac.Socket, exception: WebSocketException) = reconnect(websocket)

        fun onDisconnected(websocket: io.github.sac.Socket?, serverCloseFrame: WebSocketFrame?, clientCloseFrame: WebSocketFrame?, closedByServer: Boolean) {
            when {
                clientCloseFrame?.closeCode != WebSocketCloseCode.NORMAL -> reconnect(websocket)
                else -> clear()
            }
        }

        private fun reconnect(websocket: Socket?) {
            Single.timer(500, TimeUnit.MILLISECONDS)
                    .subscribe({ websocket?.connect() }, {})
        }

        fun onAuthentication(socket: io.github.sac.Socket, status: Boolean) {
            authoriserDisposable = socketAuthoriser
                    .authorise(this@RxSocketClusterWrapperImpl)
                    .subscribe(
                            { setStatus(Status.READY) },
                            { handleError(it) }
                    )
        }

        fun onSetAuthToken(token: String, socket: io.github.sac.Socket) {
            Log.i("socket", "onSetAuthToken")
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

    private fun setStatus(status: Status) {
        this.status = status
        stateFlowable.onNext(status)
    }

    private fun registerAuthStatusListener(socket: Socket) {
        socket.on("loginStatus", { name, data ->
            authStatusFlowable.onNext(gson.adapter(AuthStatus::class.java).fromJson(data.toString()))
        })
    }

    fun regŁisterUserChannel(channelName: String): Single<Boolean> {
        var emitter: SingleEmitter<Boolean>? = null
        val observable = Single.create<Boolean> { emitter = it }

        if (socket == null) emitter?.onSuccess(false)

        val channel = socket?.createChannel(channelName)
        channel?.subscribe { name, error, data ->
            Log.i("channel subscribed", "$name / $error / $data")
            when (error) {
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