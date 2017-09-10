package xyz.thecodeside.tradeapp.productlist

import xyz.thecodeside.tradeapp.model.Product
import xyz.thecodeside.tradeapp.mvpbase.MvpView
import xyz.thecodeside.tradeapp.mvpbase.RxBasePresenter

class ProductListPresenter : RxBasePresenter<ProductListPresenter.ProductListView>() {


    fun loadProducts() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun handleProductClick(product: Product) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    interface ProductListView : MvpView{

    }

}