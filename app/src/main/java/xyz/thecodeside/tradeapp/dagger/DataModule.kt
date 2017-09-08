package xyz.thecodeside.tradeapp.dagger

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import xyz.thecodeside.tradeapp.helpers.Logger
import xyz.thecodeside.tradeapp.repository.remote.ApiErrorHandler
import xyz.thecodeside.tradeapp.repository.remote.rest.RemoteDataSource
import xyz.thecodeside.tradeapp.repository.remote.rest.RestApiProvider
import xyz.thecodeside.tradeapp.repository.remote.socket.SocketApiProvider
import xyz.thecodeside.tradeapp.repository.remote.socket.SocketManager

import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideRemoteRepository(): RemoteDataSource = RestApiProvider().provide()

    @Provides
    @Singleton
    fun provideSocket(logger: Logger): SocketManager = SocketApiProvider(logger).provide()


    @Provides
    @Singleton
    fun provideDefaultSharedPrefs(cxt: Context): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(cxt)



    @Provides
    @Singleton
    fun provideApiErrorHandler(logger: Logger, resources: Resources): ApiErrorHandler =
            ApiErrorHandler(logger, resources)


}

