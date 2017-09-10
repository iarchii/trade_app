package xyz.thecodeside.tradeapp.repository.remote.socket

import android.util.Log
import com.google.gson.Gson
import xyz.thecodeside.tradeapp.model.*

class SocketItemPacker {

    fun pack(item: BaseSocket): String {
            val gson = Gson()
            val jsonString = gson.toJson(item, getItemClass(item.topic.name))
            return jsonString
        }

        fun unpack(message: String?): BaseSocket {
            Log.d("SOCKET", "messageString = $message")
            val gson = Gson()

            val envelopeAsMap = gson.fromJson(message, Map::class.java)
            val id = envelopeAsMap[SOCKET_TOPIC_NAME] as String? ?: throw IllegalArgumentException("Cannot find id of SocketItem in: $envelopeAsMap")

            val socketBody = gson.fromJson(envelopeAsMap[SOCKET_BODY_NAME] as String? , getItemClass(id))

            return BaseSocket(SocketTopic.valueOf(id), socketBody)
        }

}
private val socketIdMap = mapOf(
        SocketTopic.CONNECTED.name to Connected::class.java,
        SocketTopic.CONNECTED_FAILED.name to ConnectFailed::class.java
)

fun getItemClass(id: String): Class<out BaseSocketBody> = socketIdMap[id] ?: throw IllegalArgumentException("No SocketItem mapping for given id: $id")
fun getItemId(clazz: Class<out BaseSocketBody>) = socketIdMap.entries.find { it.value == clazz }?.key ?: throw IllegalArgumentException("No id found for the SocketItem given")


