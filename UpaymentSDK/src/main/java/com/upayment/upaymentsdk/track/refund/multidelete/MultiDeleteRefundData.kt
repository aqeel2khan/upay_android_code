package com.upayment.upaymentsdk.track.refund.multidelete

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class MultiDeleteRefundData {

    @SerializedName("order_id")
    @Expose
    var orderId: String? = ""

    @SerializedName("refund_order_id")
    @Expose
    var refundOrderId: String? = ""
}