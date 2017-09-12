package xyz.thecodeside.tradeapp.productlist

import android.support.v7.widget.RecyclerView
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.circle_item_view.view.*
import xyz.thecodeside.tradeapp.helpers.NumberFormatter
import xyz.thecodeside.tradeapp.helpers.calculateDiff
import xyz.thecodeside.tradeapp.helpers.invisible
import xyz.thecodeside.tradeapp.helpers.show
import xyz.thecodeside.tradeapp.model.MarketStatus
import xyz.thecodeside.tradeapp.model.Product

class ProductViewHolder(val clickListener: ProductClickListener,
                        itemView: View) : RecyclerView.ViewHolder(itemView) {
    lateinit var product : Product

    fun bind(product : Product){
        this.product = product

        itemView.productNameTv.text = product.displayName
        itemView.productPriceTv.text = NumberFormatter.format(product.currentPrice.amount, product.currentPrice.decimals)
        val diff = product.calculateDiff()

        itemView.productDiffTv.text = NumberFormatter.formatPercent(diff)
        if(product.productMarketStatus == MarketStatus.OPEN) itemView.lockIv.invisible() else itemView.lockIv.show()

        RxView.clicks(itemView).subscribe({
            clickListener.onClick(product)
        })
    }




}