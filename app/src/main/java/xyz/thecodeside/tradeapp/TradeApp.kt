package xyz.thecodeside.tradeapp

import android.app.Application
import xyz.thecodeside.tradeapp.dagger.*


class TradeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        baseComponent = DaggerBaseComponent.builder()
                .baseSystemModule(BaseSystemModule(this))
                .dataModule(DataModule())
                .utilsModule(UtilsModule())
                .build()

    }

    companion object {
        lateinit var baseComponent: BaseComponent
    }
}