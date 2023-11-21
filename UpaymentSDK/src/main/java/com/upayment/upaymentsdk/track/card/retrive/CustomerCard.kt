package com.upayment.upaymentsdk.track.card.retrive

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class CustomerCard {

    @SerializedName("brand")
    @Expose
    var brand: String? = ""

    @SerializedName("number")
    @Expose
    var number: String? = ""

    @SerializedName("scheme")
    @Expose
    var scheme: String? = ""

    @SerializedName("token")
    @Expose
    var token: String? = ""
}