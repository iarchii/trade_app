package xyz.thecodeside.tradeapp.repository.remote.rest

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import xyz.thecodeside.tradeapp.BuildConfig
import xyz.thecodeside.tradeapp.helpers.REST_API_BASE_URL
import java.util.concurrent.TimeUnit

class RestApiProvider(val token: String) {
    fun provide(): RemoteDataSource {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
                .addInterceptor(AuthorizationInterceptor(token))
        builder.connectTimeout(5, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(loggingInterceptor)
        }

        val client = builder.build()

        val retrofit: Retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(REST_API_BASE_URL)
                .client(client)
                .build()

        return retrofit.create(RemoteDataSource::class.java)
    }

}

