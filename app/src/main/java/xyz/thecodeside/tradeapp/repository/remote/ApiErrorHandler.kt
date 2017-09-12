package xyz.thecodeside.tradeapp.repository.remote

import android.content.res.Resources
import com.google.gson.GsonBuilder
import retrofit2.HttpException
import xyz.thecodeside.tradeapp.R
import xyz.thecodeside.tradeapp.helpers.Logger
import xyz.thecodeside.tradeapp.model.ResponseError
import java.io.IOException

class ApiErrorHandler(val logger: Logger,
                      val resource: Resources) {


    private val builder = GsonBuilder()

    fun handleError(exception: Throwable): ResponseError {
        logger.logException(exception)
        return when (exception) {
            is HttpException -> parseError(exception)
            is IOException -> getNetworkError()
            else -> getUnknownError()
        }
    }

    fun getNetworkError(): ResponseError = ResponseError(message = resource.getString(R.string.network_error))
    fun getUnknownError(): ResponseError = ResponseError(message = resource.getString(R.string.unknown_error))

    private fun parseError(error: HttpException) : ResponseError {
        return try {
            val gson = builder.create()
            val responseError = gson.fromJson(error.response().errorBody()?.string(), ResponseError::class.java)
            responseError ?: getUnknownError()
        } catch (e: Exception) {
            logger.logException(Throwable("JSON response could not be converted - data malformed"))
            getUnknownError()
        }
    }

}

