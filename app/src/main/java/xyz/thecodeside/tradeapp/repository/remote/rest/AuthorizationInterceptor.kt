package xyz.thecodeside.tradeapp.repository.remote.rest

import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(private val token: String?) : Interceptor {
    private val AUTHORIZATION_KEY = "Authorization"
    private val AUTH_PREFIX = "Bearer "

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val headersBuilder = request.headers().newBuilder()

        if (token!=null) {
            headersBuilder.add(AUTHORIZATION_KEY, AUTH_PREFIX + token)
        }

        val headers = headersBuilder.build()
        request = request.newBuilder().headers(headers).build()
        return chain.proceed(request)

    }

}