package xyz.thecodeside.tradeapp.productlist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import xyz.thecodeside.tradeapp.helpers.showToastShort
import kotlinx.android.synthetic.main.activity_product_list.*
import xyz.thecodeside.tradeapp.R
import xyz.thecodeside.tradeapp.TradeApp
import xyz.thecodeside.tradeapp.model.Product
import xyz.thecodeside.tradeapp.productdetails.ProductDetailsActivity
import javax.inject.Inject

class ProductListActivity : AppCompatActivity(), ProductListPresenter.ProductListView {

    @Inject lateinit var presenter: ProductListPresenter
    var adapter: ProductListAdapter = ProductListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        TradeApp.baseComponent.inject(this)
        setupRecycler()
    }

    override fun onStart() {
        presenter.attachView(this)
        super.onStart()
    }

    override fun onStop() {
        presenter.detachView()
        super.onStop()
    }


    private fun setupRecycler() {
        adapter.clickListener = object : ProductClickListener {
            override fun onClick(product: Product) {
                presenter.handleProductClick(product)
            }

        }
        productsRV.layoutManager = GridLayoutManager(this, 2)
        productsRV.adapter = adapter

    }

    override fun showProducts(productsList: MutableList<Product>) {
        adapter.setProducts(productsList)
    }

    override fun showError() {
        showToastShort(R.string.unknown_error)
    }

    override fun showProductDetails(product: Product) {
        ProductDetailsActivity.show(this, product)
    }
}

