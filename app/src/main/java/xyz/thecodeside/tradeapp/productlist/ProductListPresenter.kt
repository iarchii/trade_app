package xyz.thecodeside.tradeapp.productlist

import android.app.Activity
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import xyz.thecodeside.tradeapp.helpers.InternetConnectionManager
import xyz.thecodeside.tradeapp.helpers.Logger
import xyz.thecodeside.tradeapp.helpers.applyTransformerFlowable
import xyz.thecodeside.tradeapp.model.Product
import xyz.thecodeside.tradeapp.mvpbase.MvpView
import xyz.thecodeside.tradeapp.mvpbase.RxBasePresenter
import xyz.thecodeside.tradeapp.repository.remote.ApiErrorHandler
import javax.inject.Inject

class ProductListPresenter
@Inject
internal constructor(
        private val apiErrorHandler: ApiErrorHandler,
        private val productDetailUseCase: GetProductDetailUseCase,
        private val connectionManager: InternetConnectionManager, //TODO move it to BaseActivityPresenter
        private val logger: Logger
) : RxBasePresenter<ProductListPresenter.ProductListView>() {

    private val productsIds = mapOf(
            Pair("Germany30", "sb26493"),
            Pair("US500", "sb26496"),
            Pair("EUR/USD", "sb26502"),
            Pair("Gold", "sb26500"),
            Pair("Apple", "sb26513"),
            Pair("Deutsche Bank", "sb28248")
    )

    private val productsList = mutableListOf<Product>()

    override fun attachView(mvpView: ProductListView) {
        super.attachView(mvpView)
        connectionManager.observe(view?.getActivity())
                .subscribe({
                    handleNetworkState(it)
                },{
                    logger.logException(it)
                    handleNetworkState(InternetConnectionManager.Status.OFFLINE)
                })
                .registerInPresenter()
        loadProducts()
    }

    private fun handleNetworkState(it: InternetConnectionManager.Status?) {
        when(it){
            InternetConnectionManager.Status.ONLINE -> view?.showOnline()
            InternetConnectionManager.Status.OFFLINE -> view?.showOffline()
        }
    }

    private fun loadProducts() {
        productsList.clear()
        Flowable.fromIterable(productsIds.values)
                .observeOn(Schedulers.io())
                .flatMap { productDetailUseCase.get(it).toFlowable() }
                .onErrorResumeNext { e: Throwable ->
                    logger.logException(e)
                    Flowable.never<Product>()
                }
                .map {
                    productsList.add(it)
                    it
                }
                .compose(applyTransformerFlowable())
                .lastOrError()
                .subscribe({
                    view?.showProducts(productsList)
                }, {
                    logger.logException(it)
                    view?.showError()
                }).registerInPresenter()
    }

    fun handleProductClick(product: Product) {
        view?.showProductDetails(product)
    }

    interface ProductListView : MvpView {
        fun showProducts(productsList: MutableList<Product>)
        fun showError()
        fun showProductDetails(product: Product)
        //TODO move it to BaseActivityPresenter.BaseView
        fun getActivity(): Activity
        fun showOnline()
        fun showOffline()
    }

}

