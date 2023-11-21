package com.upayment.upaymentsdk.track.charge

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

@Parcelize
data class UpaymentGatewayEvent3(
    @SerializedName("product") val product:  MutableList<Product> = arrayListOf<Product>(),
    @SerializedName("order") val order: Order = Order(),
    @SerializedName("paymentGateway") val paymentGateway: PaymentGateway = PaymentGateway(),
    @SerializedName("language") val language: String = "en",
    @SerializedName("isSaveCard") val isSaveCard: Boolean = false,
    @SerializedName("is_whitelabled") var is_whitelabled: Boolean = false,
    @SerializedName("tokens") val tokens: Tokens = Tokens(),
    @SerializedName("reference") val reference: Reference = Reference(),
    @SerializedName("customer") val customer: Customer = Customer(),
    @SerializedName("customerExtraData") val customerExtraData: String = "",
    @SerializedName("returnUrl") val returnUrl: String = "",
    @SerializedName("cancelUrl") val cancelUrl: String = "",
    @SerializedName("notificationUrl") val notificationUrl: String = "",
    @SerializedName("notificationType") val notificationType: String = "all",
    @SerializedName("isTest") val isTest: Boolean = false,
    @SerializedName("sessionId") val sessionId: String = "",
    @SerializedName("plugin") val plugin: Plugin = Plugin(),
    @SerializedName("extraMerchantData") val  listExtraMerchantData: MutableList<ExtraMerchantDatum> = arrayListOf<ExtraMerchantDatum>()

) : Parcelable {

    companion object {
        inline fun build(block: UpaymentGatewayEvent3Builder.() -> Unit): UpaymentGatewayEvent3 {
            return UpaymentGatewayEvent3Builder().apply(block).build()
        }
    }
}

// Define the builder classes for nested classes

@Parcelize
data class Product(

    @SerializedName("description") val description: String="",
    @SerializedName("name") val name: String="",
    @SerializedName("price") val price: Float=0.0f,
    @SerializedName("qty") val qty: Int=0
) : Parcelable

@Parcelize
data class Order(
    @SerializedName("id") val id: String = "",
    @SerializedName("reference") val reference: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("currency") val currency: String = "",
    @SerializedName("amount") val amount: Float = 0.0f
) : Parcelable

@Parcelize
data class PaymentGateway(
    @SerializedName("src") var src: String = ""
) : Parcelable

@Parcelize
data class Tokens(
    @SerializedName("kFast") val kFast: String = "",
    @SerializedName("creditCard") val creditCard: String = "",
    @SerializedName("customerUniqueToken") var customerUniqueToken: String = ""
) : Parcelable

@Parcelize
data class Reference(
    @SerializedName("id") val id: String = ""
) : Parcelable

@Parcelize
data class Plugin(
    @SerializedName("src") val src: String = ""
) : Parcelable

@Parcelize
data class Customer(
    @SerializedName("uniqueId") val uniqueId: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("mobile") val mobile: String = ""
) : Parcelable

@Parcelize
data class ExtraMerchantDatum(
    @SerializedName("amount")  var amount: Float,
    @SerializedName("knetCharge") var knetCharge: String,
    @SerializedName("knetChargeType") var knetChargeType: String,
    @SerializedName("ccCharge") var ccCharge: Float=0.0f,
    @SerializedName("ccChargeType") var ccChargeType: String,
    @SerializedName("ibanNumber") var ibanNumber: String,

) : Parcelable


 class UpaymentGatewayEvent3Builder {
     private var product: MutableList<Product> = mutableListOf()
     private var order: Order = Order()
     private var paymentGateway: PaymentGateway = PaymentGateway()
     var language: String = "en"
     var isSaveCard: Boolean = false
     var is_whitelabled: Boolean = false
     private var tokens: Tokens = Tokens()
     private var reference: Reference = Reference()
     private var customer: Customer = Customer()
     private var customerExtraData: String = ""
     var returnUrl: String = ""
     var cancelUrl: String = ""
     var notificationUrl: String = ""
      var notificationType: String = "all"
      var isTest: Boolean = false
      var sessionId: String = ""
      var plugin: Plugin = Plugin()
    var listExtraMerchantData: MutableList<ExtraMerchantDatum> = mutableListOf()

//     fun product(block: ProductBuilder.() -> Unit): MutableList<Product> =
//         ProductBuilder().apply(block).build()


     fun productList(block: ProductListBuilder.() -> Unit) {

        val list= ProductListBuilder().apply(block).build()
         product.addAll(list)
     }

     fun order(block: OrderBuilder.() -> Unit) {
         order = OrderBuilder().apply(block).build()
     }

     fun paymentGateway(block: PaymentGatewayBuilder.() -> Unit) {
         paymentGateway = PaymentGatewayBuilder().apply(block).build()
     }

     fun tokens(block: TokensBuilder.() -> Unit) {
         tokens = TokensBuilder().apply(block).build()
     }

     fun reference(block: ReferenceBuilder.() -> Unit) {
         reference = ReferenceBuilder().apply(block).build()
     }

     fun customer(block: CustomerBuilder.() -> Unit) {
         customer = CustomerBuilder().apply(block).build()
     }

     fun plugin(block: PluginBuilder.() -> Unit) {
         plugin = PluginBuilder().apply(block).build()
     }

     fun extraMerchantList(block: ExtraMerchantListBuilder.() -> Unit) {

         val list= ExtraMerchantListBuilder().apply(block).build()
         listExtraMerchantData.addAll(list)
     }

    fun build(): UpaymentGatewayEvent3 {

        return UpaymentGatewayEvent3(
            product,
            order,
            paymentGateway,
            language,
            isSaveCard,
            is_whitelabled,
            tokens,
            reference,
            customer,
            customerExtraData,
            returnUrl,
            cancelUrl,
            notificationUrl,
            notificationType,
            isTest,
            sessionId,
            plugin,
            listExtraMerchantData
        )
    }




}

// DSL Builder function for UpaymentGatewayEvent3

inline fun upaymentGatewayEvent3(block: UpaymentGatewayEvent3Builder.() -> Unit): UpaymentGatewayEvent3 {
    return UpaymentGatewayEvent3Builder().apply(block).build()
}
inline fun Product(block: ProductListBuilder.() -> Unit): MutableList<Product> {
    return ProductListBuilder().apply(block).build()
}

inline fun Order(block: OrderBuilder.() -> Unit): Order {
    return OrderBuilder().apply(block).build()
}


