package com.upayment.upaymentsdk.track.card.retrive

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class CardData {

    @SerializedName("customerCards")
    @Expose
    var customerCards: List<CustomerCard>? = null
}