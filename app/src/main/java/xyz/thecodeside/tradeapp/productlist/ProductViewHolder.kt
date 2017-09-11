package xyz.thecodeside.tradeapp.productlist

import android.support.v7.widget.RecyclerView
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.circle_item_view.view.*
import xyz.thecodeside.tradeapp.helpers.NumberFormatter
import xyz.thecodeside.tradeapp.helpers.calculateDiff
import xyz.thecodeside.tradeapp.model.Product

class ProductViewHolder(val clickListener: ProductClickListener,
                        itemView: View) : RecyclerView.ViewHolder(itemView) {
    lateinit var product : Product

    fun bind(product : Product){
        this.product = product

        itemView.productName.text = product.displayName
        itemView.productPrice.text = NumberFormatter.format(product.currentPrice.amount, product.currentPrice.decimals)
        val diff = product.calculateDiff()

        itemView.productDiff.text = NumberFormatter.formatPercent(diff)
        RxView.clicks(itemView).subscribe({
            clickListener.onClick(product)
        })
    }




}