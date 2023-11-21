package com.upayment.upaymentsdk.track.charge

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ProductBuilder : Parcelable {
   private var productList: MutableList<Product> = mutableListOf()
    var description: String = ""
    var name: String = ""
    var price: Float = 0.0f
    var qty: Int = 0
    fun build(): Product = Product(description, name, price, qty)
}



@Parcelize
class ProductListBuilder : Parcelable {
    private val productList: MutableList<Product> = mutableListOf()

    fun product(block: ProductBuilder.() -> Unit) {
        val product = ProductBuilder().apply(block).build()
        productList.add(product)
    }

    fun build(): MutableList<Product> = productList
}