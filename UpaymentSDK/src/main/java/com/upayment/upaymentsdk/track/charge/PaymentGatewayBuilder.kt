package com.upayment.upaymentsdk.track.charge

class PaymentGatewayBuilder {
    // Properties that match the fields in PaymentGateway class
    var src: String = ""

    fun build(): PaymentGateway {
        return PaymentGateway(
            src = src,
            // Set other properties here
        )
    }
}