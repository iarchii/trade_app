package xyz.thecodeside.tradeapp.repository.remote.socket

import android.util.Log
import com.google.gson.Gson
import xyz.thecodeside.tradeapp.model.BaseSocket
import xyz.thecodeside.tradeapp.model.SocketTopic
import xyz.thecodeside.tradeapp.model.TOPIC_NAME

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
            val id = envelopeAsMap[TOPIC_NAME] as String? ?: throw IllegalArgumentException("Cannot find id of SocketItem in: $envelopeAsMap")

            val socketItem = gson.fromJson(message, getItemClass(id))

            return socketItem
        }

}
private val socketIdMap = mapOf(
        SocketTopic.CONNECTED.name to BaseSocket::class.java,
        SocketTopic.CONNECTED_FAILED.name to BaseSocket::class.java
)

fun getItemClass(id: String): Class<out BaseSocket> = socketIdMap[id] ?: throw IllegalArgumentException("No SocketItem mapping for given id: $id")
fun getItemId(clazz: Class<out BaseSocket>) = socketIdMap.entries.find { it.value == clazz }?.key ?: throw IllegalArgumentException("No id found for the SocketItem given")


