package com.upayment.upaymentsdk.track.refund.multirefund

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class ResponseMultivendorRefund {

    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null
}