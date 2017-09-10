package xyz.thecodeside.tradeapp.productlist

import android.support.v7.widget.RecyclerView
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import xyz.thecodeside.tradeapp.model.Product

class ProductViewHolder(val clickListener: ProductClickListener,
                        itemView: View) : RecyclerView.ViewHolder(itemView) {
    lateinit var product : Product

    fun bind(product : Product){
        this.product = product

        RxView.clicks(itemView).subscribe({
            clickListener.onClick(product)
        })
    }

}