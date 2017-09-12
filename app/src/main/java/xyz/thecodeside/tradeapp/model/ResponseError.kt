package xyz.thecodeside.tradeapp.model

data class ResponseError(
		var message: String? = null,// may be null
		var developerMessage: String? = null,// technical description of the error
		var errorCode: ErrorCode? = null// AUTH_001
): BaseSocketBody
