package xyz.thecodeside.tradeapp.model

data class ConnectFailed(
        val developerMessage : String,
        val errorCode : String

): BaseSocketBody