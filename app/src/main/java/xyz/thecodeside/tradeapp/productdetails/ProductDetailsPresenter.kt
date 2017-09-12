package xyz.thecodeside.tradeapp.productdetails

import android.util.Log
import com.google.gson.Gson
import xyz.thecodeside.tradeapp.helpers.Logger
import xyz.thecodeside.tradeapp.helpers.NumberFormatter
import xyz.thecodeside.tradeapp.helpers.applyTransformerFlowable
import xyz.thecodeside.tradeapp.helpers.calculateDiff
import xyz.thecodeside.tradeapp.model.*
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
        fun showDiff(calculatedDiff: Float)
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
                   Log.d(SocketManager.TAG, "Message object = ${Gson().toJson(it)}")
                    when(it.type){
                        SocketType.TRADING_QUOTE -> updateProduct((it.body as TradingQuote).currentPrice)
                    }
                },{
                  view?.showError(apiErrorHandler.handleError(it))
                })

        socket.connect()
                .compose(applyTransformerFlowable())
                .subscribe({
                    Log.d(SocketManager.TAG, "CONNECTED & READY")
                    observeProduct(product.securityId)
                },{
                    logger.logException(it)
                })

    }

    private fun updateProduct(updatedPrice: Float) {
        product.currentPrice.amount = updatedPrice
        showCurrentPrice(product)
    }

    private fun observeProduct(id: String){
        val request = SocketRequest(subscribeProduct = listOf(SubscriptionProduct(id)))
        socket.send(request)
    }



    private fun showProduct(product: Product) {
        view?.showProductDetails(product.displayName, product.symbol, product.securityId)
        view?.showClosingPrice(formatPrice(product.closingPrice))

        showCurrentPrice(product)
    }

    private fun showCurrentPrice(product: Product) {
        view?.showCurrentPrice(formatPrice(product.currentPrice))
        view?.showDiff(product.calculateDiff())
    }

    private fun formatPrice(price: Price): String {
        val formattedValue = NumberFormatter.format(price.amount, price.decimals)
        val currency = Currency.getInstance(price.currency)
        return  "${currency.symbol}$formattedValue"
    }


}