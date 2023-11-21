package com.upayment.upaymentsdk.track.charge

class CustomerBuilder {
    // Properties that match the fields in Customer class
    var uniqueId: String = ""
    var name: String = ""
    var email: String = ""
    var mobile: String = ""

    fun build(): Customer {
        return Customer(
            uniqueId = uniqueId,
            name = name,
            email = email,
            mobile = mobile
        )
    }
}