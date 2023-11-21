package com.upayment.upaymentsdk.track.card

data class Expiry(val month: String, val year: String)
data class Card(val number: String, val expiry: Expiry, val securityCode: String, val nameOnCard: String)
data class Customer(val card: Card, var customerUniqueToken: Long)

class ExpiryBuilder {
    var month: String = ""
    var year: String = ""

    fun build(): Expiry {
        return Expiry(month, year)
    }
}

class CardBuilder {
    var number: String = ""
    var expiry: ExpiryBuilder.() -> Unit = {}
    var securityCode: String = ""
    var nameOnCard: String = ""

    fun expiry(block: ExpiryBuilder.() -> Unit) {
        expiry = block
    }

    fun build(): Card {
        val expiryBuilder = ExpiryBuilder().apply(expiry)
        return Card(number, expiryBuilder.build(), securityCode, nameOnCard)
    }
}

class CustomerBuilder {
    var card: CardBuilder.() -> Unit = {}
    var customerUniqueToken: Long = 0

    fun card(block: CardBuilder.() -> Unit) {
        card = block
    }

    fun build(): Customer {
        val cardBuilder = CardBuilder().apply(card)
        return Customer(cardBuilder.build(), customerUniqueToken)
    }
}

fun customer(block: CustomerBuilder.() -> Unit): Customer {
    return CustomerBuilder().apply(block).build()
}

