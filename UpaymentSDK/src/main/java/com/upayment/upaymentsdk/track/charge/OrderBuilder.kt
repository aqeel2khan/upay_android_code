package com.upayment.upaymentsdk.track.charge

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class OrderBuilder : Parcelable {
    var id: String = ""
    var reference: String = ""
    var description: String = ""
    var currency: String = ""
    var amount: Float = 0.0f

    fun build(): Order = Order(id, reference, description, currency, amount)
}