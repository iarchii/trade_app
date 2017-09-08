package xyz.thecodeside.tradeapp.dagger

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(BaseSystemModule::class, DataModule::class, UtilsModule::class))
interface BaseComponent {

}

