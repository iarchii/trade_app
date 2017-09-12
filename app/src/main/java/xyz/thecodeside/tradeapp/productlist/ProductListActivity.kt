package xyz.thecodeside.tradeapp.productlist

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.progress_overlay.*
import xyz.thecodeside.tradeapp.R
import xyz.thecodeside.tradeapp.TradeApp
import xyz.thecodeside.tradeapp.base.BaseActivity
import xyz.thecodeside.tradeapp.helpers.hide
import xyz.thecodeside.tradeapp.helpers.show
import xyz.thecodeside.tradeapp.helpers.showToastShort
import xyz.thecodeside.tradeapp.model.Product
import xyz.thecodeside.tradeapp.productdetails.ProductDetailsActivity
import javax.inject.Inject

class ProductListActivity : BaseActivity(), ProductListPresenter.ProductListView {
    override fun showProgress() {
        progressOverlay.show()
    }

    override fun getActivity(): Activity = this

    override fun showOnline() {
        hideOfflineMessage()
    }

    override fun showOffline() {
        showOfflineMessage(findViewById(android.R.id.content))
    }

    @Inject lateinit var presenter: ProductListPresenter
    var adapter: ProductListAdapter = ProductListAdapter()

    private val PRODUCT_COLUMNS = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        TradeApp.baseComponent.inject(this)
        setupRecycler()
        presenter.attachView(this)
    }



    override fun onDestroy() {
        presenter.detachView()
        super.onStop()
    }

    private fun setupRecycler() {
        adapter.clickListener = object : ProductClickListener {
            override fun onClick(product: Product) {
                presenter.handleProductClick(product)
            }
        }

        productsRV.layoutManager = GridLayoutManager(this, PRODUCT_COLUMNS)
        productsRV.addItemDecoration(GridItemDecoration(
                resources.getDimensionPixelSize(R.dimen.item_spacing),
                PRODUCT_COLUMNS))
        productsRV.adapter = adapter

    }

    override fun showProducts(productsList: MutableList<Product>) {
        progressOverlay.hide()
        adapter.setProducts(productsList)
    }

    override fun showError(message: String?) {
        showToastShort(message ?: getString(R.string.unknown_error))
    }

    override fun showProductDetails(product: Product) {
        ProductDetailsActivity.show(this, product)
    }
}

