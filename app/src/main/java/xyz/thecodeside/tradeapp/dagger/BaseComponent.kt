package xyz.thecodeside.tradeapp.dagger

import dagger.Component
import xyz.thecodeside.tradeapp.productdetails.ProductDetailsActivity
import xyz.thecodeside.tradeapp.productlist.ProductListActivity
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(BaseSystemModule::class, DataModule::class, UtilsModule::class))
interface BaseComponent {
    fun inject(view: ProductListActivity)
    fun inject(view: ProductDetailsActivity)
}

