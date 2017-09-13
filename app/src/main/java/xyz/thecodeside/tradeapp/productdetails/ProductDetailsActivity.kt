package xyz.thecodeside.tradeapp.productdetails

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_product_details.*
import xyz.thecodeside.tradeapp.R
import xyz.thecodeside.tradeapp.TradeApp
import xyz.thecodeside.tradeapp.base.BaseActivity
import xyz.thecodeside.tradeapp.helpers.NumberFormatter
import xyz.thecodeside.tradeapp.helpers.showToastShort
import xyz.thecodeside.tradeapp.model.Product
import javax.inject.Inject

class ProductDetailsActivity : BaseActivity(), ProductDetailsPresenter.ProductDetailsView {
    override fun showOnline() = hideOfflineMessage()

    override fun showOffline() {
        showOfflineMessage(rootView = findViewById(android.R.id.content))
    }

    override fun getActivity(): Activity = this

    override fun showLockedMarket() {
        statusIv.setImageResource(R.mipmap.ic_lock)
        statusTv.setText(R.string.market_locked)
    }

    override fun showOfflineMarket() {
        statusIv.setImageResource(R.mipmap.ic_offline)
        statusTv.setText(R.string.market_offline)
    }

    override fun showOnlineMarket() {
        statusIv.setImageResource(R.mipmap.ic_online)
        statusTv.setText(R.string.market_online)
    }

    override fun showDiff(calculatedDiff: Float) {
        priceDiffPercentTv.text = NumberFormatter.formatPercent(calculatedDiff)
    }

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

    override fun showError(message : String?) =
            showToastShort(message ?: getString(R.string.unknown_error))

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

    }

    override fun onResume() {
        super.onStart()
        val product = intent?.getSerializableExtra(PRODUCT_KEY) as Product?
        presenter.attachView(this, product)

    }
    override fun onPause() {
        presenter.detachView()
        super.onPause()
    }



}

