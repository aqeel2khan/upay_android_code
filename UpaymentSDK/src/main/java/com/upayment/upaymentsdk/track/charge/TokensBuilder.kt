package com.upayment.upaymentsdk.track.charge

class TokensBuilder {
    // Properties that match the fields in Tokens class
    var kFast: String = ""
    var creditCard: String = ""
    var customerUniqueToken: String = ""

    fun build(): Tokens {
        return Tokens(
            kFast = kFast,
            creditCard = creditCard,
            customerUniqueToken = customerUniqueToken
        )
    }
}