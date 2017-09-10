package xyz.thecodeside.tradeapp.productdetails

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import xyz.thecodeside.tradeapp.model.Product

class ProductDetailsActivity  : AppCompatActivity(){
    companion object {
        private const val PRODUCT_KEY = "PRODUCT_KEY"
        fun show(context: Context, product: Product){
            val intent = Intent(context, ProductDetailsActivity::class.java)
            context.startActivity(intent)
        }
    }

}