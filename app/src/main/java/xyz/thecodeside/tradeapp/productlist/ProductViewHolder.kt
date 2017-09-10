package xyz.thecodeside.tradeapp.productlist

import android.support.v7.widget.RecyclerView
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.circle_item_view.view.*
import xyz.thecodeside.tradeapp.model.Product

class ProductViewHolder(val clickListener: ProductClickListener,
                        itemView: View) : RecyclerView.ViewHolder(itemView) {
    lateinit var product : Product

    fun bind(product : Product){
        this.product = product

        itemView.productName.text = product.displayName
        itemView.productPrice.text = product.currentPrice.amount


        RxView.clicks(itemView).subscribe({
            clickListener.onClick(product)
        })
    }

}