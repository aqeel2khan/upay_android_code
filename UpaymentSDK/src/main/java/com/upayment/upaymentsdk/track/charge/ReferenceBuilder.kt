package com.upayment.upaymentsdk.track.charge

class ReferenceBuilder {
    // Properties that match the fields in Reference class
    var id: String = ""

    fun build(): Reference {
        return Reference(
            id = id
        )
    }
}