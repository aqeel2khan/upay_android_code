package com.upayment.upaymentsdk.track.refund.multirefund

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class Generated {

    @SerializedName("generatedInvoiceId")
    @Expose
    var generatedInvoiceId: String? = ""

    @SerializedName("amount")
    @Expose
    var amount: Int? = 0

    @SerializedName("orderId")
    @Expose
    var orderId: String? = ""

    @SerializedName("refundOrderId")
    @Expose
    var refundOrderId: String? = ""

    @SerializedName("refundArn")
    @Expose
    var refundArn: String? = ""
}