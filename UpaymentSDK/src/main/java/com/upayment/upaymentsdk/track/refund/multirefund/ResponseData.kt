package com.upayment.upaymentsdk.track.refund.multirefund

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class ResponseData {

    @SerializedName("generated")
    @Expose
    var generated: List<Generated>? = null

    @SerializedName("dataTempered")
    @Expose
    var dataTempered: List<Any>? = null

    @SerializedName("refundIssue")
    @Expose
    var refundIssue: List<Any>? = null

    @SerializedName("insufficientBalance")
    @Expose
    var insufficientBalance: List<Any>? = null
}