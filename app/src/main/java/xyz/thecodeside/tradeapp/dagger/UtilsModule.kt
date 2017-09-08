package xyz.thecodeside.tradeapp.dagger

import dagger.Module
import dagger.Provides
import xyz.thecodeside.tradeapp.helpers.Logger

@Module
class UtilsModule {

    @Provides
    fun provideLogger(): Logger {
        return object : Logger{
            override fun logException(throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

}