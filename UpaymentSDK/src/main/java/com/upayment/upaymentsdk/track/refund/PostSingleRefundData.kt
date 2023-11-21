package com.upayment.upaymentsdk.track.refund

import com.google.gson.annotations.SerializedName
import com.upayment.upaymentsdk.track.charge.Product

class PostSingleRefundData {
        var  orderId:String =""
        var totalPrice: Float= 0.0f
        var customerFirstName:String=""
        var customerEmail: String = ""
        var customerMobileNumber:String= ""
        var reference: String= ""
        var notifyUrl:String= ""
        var receiptId:String= ""
         var refundPayload:  MutableList<PayloadData> = arrayListOf<PayloadData>()

}