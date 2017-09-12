package xyz.thecodeside.tradeapp.repository.remote.socket

import android.util.Log
import com.google.gson.Gson
import xyz.thecodeside.tradeapp.model.*

class SocketItemPacker {

    fun pack(item: BaseSocket): String {
    /*    val gson = Gson()
        val jsonString = gson.toJson(item, getItemClass(item.type.name))
        return jsonString*/
        return ""
    }

    fun unpack(message: String?): BaseSocket {
        Log.d(SocketManager.TAG, "messageString = $message")
        val gson = Gson()

        val envelopeAsMap = gson.fromJson(message, Map::class.java)
        val idString = envelopeAsMap[SOCKET_TOPIC_NAME] as String? ?: throw IllegalArgumentException("Cannot find id of SocketType in: $envelopeAsMap")
        val id = gson.fromJson(idString, SocketType::class.java) ?: throw IllegalArgumentException("No SocketType mapping for given id: $idString")
        val body =  envelopeAsMap[SOCKET_BODY_NAME].toString()
        val socketBody = gson.fromJson(body , getItemClass(id))

        return BaseSocket(id, socketBody)
    }

}
private val socketIdMap = mapOf(
        SocketType.CONNECT_CONNECTED to Connected::class.java,
        SocketType.CONNECT_FAILED to ConnectFailed::class.java,
        SocketType.PORTFOLIO_PERFORMANCE to PortfolioPerformance::class.java
)

fun getItemClass(id: SocketType): Class<out BaseSocketBody> = socketIdMap[id] ?: throw IllegalArgumentException("No SocketType mapping for given id: $id")
fun getItemId(clazz: Class<out BaseSocketBody>) = socketIdMap.entries.find { it.value == clazz }?.key ?: throw IllegalArgumentException("No id found for the SocketType given")


