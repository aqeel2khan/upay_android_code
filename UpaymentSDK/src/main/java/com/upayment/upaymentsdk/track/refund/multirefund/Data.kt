package com.upayment.upaymentsdk.track.refund.multirefund

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class Data {

    @SerializedName("responseData")
    @Expose
    var responseData: ResponseData? = null
}