package xyz.thecodeside.tradeapp

import android.app.Application
import xyz.thecodeside.tradeapp.dagger.*


class TradeApp : Application() {
    companion object {
        lateinit var baseComponent: BaseComponent
    }

    override fun onCreate() {
        super.onCreate()

        baseComponent = DaggerBaseComponent.builder()
                .baseSystemModule(BaseSystemModule(this))
                .dataModule(DataModule())
                .utilsModule(UtilsModule())
                .build()
    }
}