package xyz.thecodeside.tradeapp.productlist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_product_list.*
import xyz.thecodeside.tradeapp.R
import xyz.thecodeside.tradeapp.TradeApp
import xyz.thecodeside.tradeapp.model.Product
import javax.inject.Inject

class ProductListActivity : AppCompatActivity() {

    @Inject lateinit var presenter: ProductListPresenter
    var adapter: ProductListAdapter = ProductListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        TradeApp.baseComponent.inject(this)

        setupRecycler()
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
}

