package com.upayment.upaymentsdk.track.refund
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseMultivendorRefundData {

    //


    //


    @SerializedName("isMultivendorRefund")
    @Expose
    var isMultivendorRefund: Boolean? = false

    @SerializedName("orderId")
    @Expose
    var orderId: String? = null

    @SerializedName("refundOrderId")
    @Expose
    var refundOrderId: String? = ""

    @SerializedName("refundArn")
    @Expose
    var refundArn: String? = ""

    @SerializedName("refundPayload")
    @Expose
    var refundPayload: MutableList<RefundPayload>? = arrayListOf()
}

class RefundPayload {
    @SerializedName("refundRequestId")
    @Expose
    var refundRequestId: String = ""
    @SerializedName("ibanNumber")
    @Expose
    var ibanNumber: String = ""
    @SerializedName("totalPaid")
    @Expose
    var totalPaid: String = ""
    @SerializedName("refundedAmount")
    @Expose
    var refundedAmount: Float = 0.0f
    @SerializedName("remainingLimit")
    @Expose
    var remainingLimit: Float = 0.0f
    @SerializedName("amountToRefund")
    @Expose
    var amountToRefund: Float = 0.0f
    @SerializedName("merchantType")
    @Expose
    var merchantType: String = ""
}
