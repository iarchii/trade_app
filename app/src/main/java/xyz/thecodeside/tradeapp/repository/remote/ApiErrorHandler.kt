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
        when (exception) {
            is HttpException -> return parseError(exception)
            is IOException -> return getNetworkError()
            else ->return getUnknownError()
        }
    }

    fun getNetworkError(): ResponseError = ResponseError(resource.getString(R.string.network_error))
    fun getUnknownError(): ResponseError = ResponseError(resource.getString(R.string.unknown_error))

    private fun parseError(error: HttpException) : ResponseError {
        try {
            val gson = builder.create()
            val responseError = gson.fromJson(error.response().errorBody()?.string(), ResponseError::class.java)
            return responseError ?: getUnknownError()
        } catch (e: Exception) {
            logger.logException(Throwable("JSON response could not be converted - data malformed"))
            return getUnknownError()
        }
    }

}

