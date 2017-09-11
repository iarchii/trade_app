package xyz.thecodeside.tradeapp.productdetails

import xyz.thecodeside.tradeapp.model.Price
import xyz.thecodeside.tradeapp.model.Product
import xyz.thecodeside.tradeapp.mvpbase.MvpView
import xyz.thecodeside.tradeapp.mvpbase.RxBasePresenter
import xyz.thecodeside.tradeapp.productlist.GetProductDetailUseCase
import xyz.thecodeside.tradeapp.repository.remote.ApiErrorHandler
import xyz.thecodeside.tradeapp.repository.remote.socket.SocketManager
import javax.inject.Inject

class ProductDetailsPresenter
@Inject
internal constructor(
        private val apiErrorHandler: ApiErrorHandler,
        private val productDetailUseCase: GetProductDetailUseCase,
        private val socket : SocketManager
) : RxBasePresenter<ProductDetailsPresenter.ProductDetailsView>(){
    interface ProductDetailsView : MvpView {
        fun showError()
        fun showClosingPrice(price: Price)
        fun showCurrentPrice(price: Price)
        fun showProductDetails(displayName: String, symbol: String, securityId: String)
    }

    private lateinit var product : Product

    fun attachView(mvpView: ProductDetailsView, product: Product?) {
        super.attachView(mvpView)
        setProduct(product)
    }

    private fun setProduct(product: Product?) {
        if (product == null) {
            view?.showError()
        } else {
            this.product = product
            view?.showProductDetails(product.displayName,product.symbol, product.securityId)
            view?.showCurrentPrice(product.currentPrice)
            view?.showClosingPrice(product.closingPrice)
        }
    }

}