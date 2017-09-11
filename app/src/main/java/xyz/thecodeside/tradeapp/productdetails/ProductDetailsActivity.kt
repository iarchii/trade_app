package xyz.thecodeside.tradeapp.productdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_product_details.*
import xyz.thecodeside.tradeapp.R
import xyz.thecodeside.tradeapp.TradeApp

import xyz.thecodeside.tradeapp.model.Product
import javax.inject.Inject

class ProductDetailsActivity : AppCompatActivity(), ProductDetailsPresenter.ProductDetailsView {
    override fun showProductDetails(displayName: String, symbol: String, securityId : String) {
        displayNameTv.text = displayName
        symbolTv.text = symbol
        idTv.text = getString(R.string.id_format,securityId)
    }

    override fun showClosingPrice(price: String) {
        closingPriceTv.text = price
    }

    override fun showCurrentPrice(price: String) {
        currentPriceTv.text = price
    }


    override fun showError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private const val PRODUCT_KEY = "PRODUCT_KEY"
        fun show(context: Context, product: Product) {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra(PRODUCT_KEY, product)
            context.startActivity(intent)
        }
    }

    @Inject lateinit var presenter: ProductDetailsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        TradeApp.baseComponent.inject(this)
        val product = intent?.getSerializableExtra(PRODUCT_KEY) as Product?
        presenter.attachView(this, product)
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }


}

