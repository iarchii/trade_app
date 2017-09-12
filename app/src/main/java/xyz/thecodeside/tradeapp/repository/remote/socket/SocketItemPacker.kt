package xyz.thecodeside.tradeapp.repository.remote.socket

import android.util.Log
import com.google.gson.Gson
import org.json.JSONObject
import xyz.thecodeside.tradeapp.model.*

class SocketItemPacker {

    fun pack(item: SocketRequest): String {
        val messageString = Gson().toJson(item)
        Log.d(SocketManager.TAG, "sendMessage = $messageString")
        return messageString
    }

    fun unpack(message: String?): BaseSocket {
        Log.d(SocketManager.TAG, "messageString = $message")
        val gson = Gson()
        val json = JSONObject(message)
        json.has(SOCKET_TOPIC_NAME)
        val idString = if(json.has(SOCKET_TOPIC_NAME)) json.getString(SOCKET_TOPIC_NAME) else throw IllegalArgumentException("Cannot find id of SocketType in: $json")
        val id = gson.fromJson(idString, SocketType::class.java) ?: throw IllegalArgumentException("No SocketType mapping for given id: $idString")
        val body =  if(json.has(SOCKET_BODY_NAME)) json.getString(SOCKET_BODY_NAME) else throw IllegalArgumentException("Cannot find body of SocketMessage in: $json")
        val socketBody = gson.fromJson(body , getItemClass(id))

        return BaseSocket(id, socketBody)
    }

}
private val socketIdMap = mapOf(
        SocketType.CONNECT_CONNECTED to Connected::class.java,
        SocketType.CONNECT_FAILED to ResponseError::class.java,
        SocketType.TRADING_QUOTE to TradingQuote::class.java,
        SocketType.PORTFOLIO_PERFORMANCE to PortfolioPerformance::class.java
)


fun getItemClass(id: SocketType): Class<out BaseSocketBody> = socketIdMap[id] ?: throw IllegalArgumentException("No SocketType mapping for given id: $id")


