package com.upayment.upaymentsdk.track.refund.multidelete

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class ResponseMultiRefundDelete {

    @SerializedName("status")
    @Expose
    var status: Boolean? = false

    @SerializedName("message")
    @Expose
    var message: String? = ""

    @SerializedName("data")
    @Expose
    var data: MultiDeleteRefundData? = MultiDeleteRefundData()
}