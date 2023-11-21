package com.upayment.upaymentsdk.track.card.addcard

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class ResponseAddCard {

    @SerializedName("status")
    @Expose
    var status: Boolean? = false

    @SerializedName("message")
    @Expose
    var message: String? = ""

    @SerializedName("data")
    @Expose
    var data: DataAddCard? = DataAddCard()
}