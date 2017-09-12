package xyz.thecodeside.tradeapp.productdetails

import android.app.Activity
import xyz.thecodeside.tradeapp.helpers.*
import xyz.thecodeside.tradeapp.model.*
import xyz.thecodeside.tradeapp.mvpbase.MvpView
import xyz.thecodeside.tradeapp.mvpbase.RxBasePresenter
import xyz.thecodeside.tradeapp.repository.remote.ApiErrorHandler
import xyz.thecodeside.tradeapp.repository.remote.socket.RxSocketWrapper
import xyz.thecodeside.tradeapp.repository.remote.socket.SocketManager
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ProductDetailsPresenter
@Inject
internal constructor(
        private val apiErrorHandler: ApiErrorHandler,
        private val socket: SocketManager,
        private val connectionManager: InternetConnectionManager,
        private val logger: Logger
) : RxBasePresenter<ProductDetailsPresenter.ProductDetailsView>() {
    interface ProductDetailsView : MvpView {
        fun showError(message: String?)
        fun showClosingPrice(price: String)
        fun showCurrentPrice(price: String)
        fun showProductDetails(displayName: String, symbol: String, securityId: String)
        fun showDiff(calculatedDiff: Float)
        fun showLockedMarket()
        fun showOfflineMarket()
        fun showOnlineMarket()
        //TODO better way to handle Receivers?
        fun getActivity(): Activity

        fun showOnline()
        fun showOffline()
    }

    private val MESSAGE_SAMPLING_MILLISECONDS = 100L
    private lateinit var product: Product

    fun attachView(mvpView: ProductDetailsView, product: Product?) {
        super.attachView(mvpView)
        init(product)
    }

    private fun init(product: Product?) {
        handleProduct(product)
        connectionManager.observe(view?.getActivity())
                .subscribe({
                    handleNetworkState(it)
                }, {
                    logger.logException(it)
                    handleNetworkState(InternetConnectionManager.Status.OFFLINE)
                })
                .registerInPresenter()
    }

    private fun handleNetworkState(it: InternetConnectionManager.Status?) {
        when (it) {
            InternetConnectionManager.Status.ONLINE -> {
                view?.showOnline()
                initMarketOnline(product)
            }
            InternetConnectionManager.Status.OFFLINE -> {
                view?.showOffline()
            }
        }
    }

    override fun detachView() {
        clean()
        super.detachView()
    }

    private fun clean() {
        connectionManager.stopObserve(view?.getActivity())
        unsubscribeProduct(product.securityId)
        socket.disconnect()
    }

    private fun unsubscribeProduct(id: String) {
        val request = SocketRequest(unsubscribeProduct = listOf(SubscriptionProduct(id)))
        socket.send(request)
    }

    private fun handleProduct(product: Product?) {
        product?.let {
            this.product = product
            showProduct(product)
        } ?: view?.showError(apiErrorHandler.getUnknownError().message)
    }

    private fun initMarketOnline(product: Product) {
        if (isMarketLocked(product.productMarketStatus)) {
            view?.showLockedMarket()
        } else {
            initSocket()
        }
    }

    private fun isMarketLocked(productMarketStatus: MarketStatus): Boolean = productMarketStatus == MarketStatus.CLOSED

    private fun initSocket() {
        view?.showOfflineMarket()
        observeSocketMessages()

        connectToSocket()

    }

    private fun connectToSocket() {
        socket.connect()
                .compose(applyTransformerFlowable())
                .subscribe({
                    when (it) {
                        RxSocketWrapper.Status.READY -> {
                            view?.showOnlineMarket()
                            observeProduct(product.securityId)
                        }
                        else -> view?.showOfflineMarket()
                    }

                }, {
                    logger.logException(it)
                }).registerInPresenter()
    }

    private fun observeSocketMessages() {
        socket.observe()
                .filter { it.type == SocketType.TRADING_QUOTE }
                .sample(MESSAGE_SAMPLING_MILLISECONDS, TimeUnit.MILLISECONDS)
                .compose(applyTransformerFlowable())
                .subscribe({
                    updateProduct((it.body as TradingQuote))
                }, {
                    view?.showError(apiErrorHandler.handleError(it).message)
                }).registerInPresenter()
    }

    private fun updateProduct(updatedProduct: TradingQuote) {
        if (updatedProduct.securityId == product.securityId) {
            product.currentPrice.amount = updatedProduct.currentPrice
            showCurrentPrice(product)
        }
    }

    private fun observeProduct(id: String) {
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
        return "${currency.symbol}$formattedValue"
    }


}