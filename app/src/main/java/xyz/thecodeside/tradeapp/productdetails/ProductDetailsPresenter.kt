package xyz.thecodeside.tradeapp.productdetails

import xyz.thecodeside.tradeapp.helpers.*
import xyz.thecodeside.tradeapp.model.Price
import xyz.thecodeside.tradeapp.model.Product
import xyz.thecodeside.tradeapp.model.ResponseError
import xyz.thecodeside.tradeapp.mvpbase.MvpView
import xyz.thecodeside.tradeapp.mvpbase.RxBasePresenter
import xyz.thecodeside.tradeapp.repository.remote.ApiErrorHandler
import xyz.thecodeside.tradeapp.repository.remote.socket.SocketManager
import java.util.*
import javax.inject.Inject

class ProductDetailsPresenter
@Inject
internal constructor(
        private val apiErrorHandler: ApiErrorHandler,
        private val socket : SocketManager,
        private val logger: Logger
) : RxBasePresenter<ProductDetailsPresenter.ProductDetailsView>(){
    interface ProductDetailsView : MvpView {
        fun showError(handleError: ResponseError)
        fun showClosingPrice(price: String)
        fun showCurrentPrice(price: String)
        fun showProductDetails(displayName: String, symbol: String, securityId: String)
        fun showDiff(calculateDiff: Float)
    }

    private lateinit var product : Product

    fun attachView(mvpView: ProductDetailsView, product: Product?) {
        super.attachView(mvpView)
        handleProduct(product)

    }

    private fun handleProduct(product: Product?) {
        if (product == null) {
            view?.showError(apiErrorHandler.getUnknownError())
        } else {
            this.product = product
            showProduct(product)
            initSocket()
        }
    }

    private fun initSocket() {
        socket.observe()
                .compose(applyTransformerFlowable())
                .subscribe({
                    println(it)
                },{
                  view?.showError(apiErrorHandler.handleError(it))
                })

        socket.connect()
                .compose(applyTransformerCompletable())
                .subscribe({
                    println("CONNECTED")
                },{
                    logger.logException(it)
                })

    }

    private fun showProduct(product: Product) {
        view?.showProductDetails(product.displayName, product.symbol, product.securityId)
        view?.showCurrentPrice(formatPrice(product.currentPrice))
        view?.showClosingPrice(formatPrice(product.closingPrice))
        view?.showDiff(product.calculateDiff())
    }

    private fun formatPrice(price: Price): String {
        val formattedValue = NumberFormatter.format(price.amount, price.decimals)
        val currency = Currency.getInstance(price.currency)
        return  "${currency.symbol}$formattedValue"
    }


}