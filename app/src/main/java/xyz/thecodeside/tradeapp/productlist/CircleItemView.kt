package xyz.thecodeside.tradeapp.productlist

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import xyz.thecodeside.tradeapp.R


class CircleItemView : RelativeLayout {
    private lateinit var root : RelativeLayout

    constructor(context: Context?) : super(context) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    fun init(context: Context?) {
        val li = LayoutInflater.from(context)
        root = li.inflate(R.layout.circle_item_view,this,true) as RelativeLayout
    }

}
