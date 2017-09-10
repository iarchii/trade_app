package xyz.thecodeside.tradeapp.productlist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import xyz.thecodeside.tradeapp.R
import xyz.thecodeside.tradeapp.model.Product

class ProductListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val products: MutableList<Product> = mutableListOf()
    lateinit var clickListener: ProductClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProductViewHolder(clickListener, inflater.inflate(R.layout.circle_item_view, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as ProductViewHolder).bind(products[position])
    }

    override fun getItemCount(): Int = products.size


}

