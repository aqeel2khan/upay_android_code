package com.upayment.upaymentsdk.track

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.upayment.upaymentsdk.track.UpaymentGatewayJsnUtils.Companion.merge
import com.upayment.upaymentsdk.track.UpaymentGatewayJsnUtils.Companion.put
import com.upayment.upaymentsdk.util.UpaymentGatewayAppUtils
import kotlinx.parcelize.Parcelize
import org.json.JSONObject
@Parcelize
open class UpaymentGatewayEvent2 (
    @SerializedName("product")   val product: Product= Product(),
    @SerializedName("order")   val order: Order = Order(),
    @SerializedName("paymentGateway")   val paymentGateway: PaymentGateway=PaymentGateway(),
    @SerializedName("language")   val language: String ="en",
    @SerializedName("isSaveCard")   val isSaveCard: Boolean= false,
    @SerializedName("is_whitelabled") val is_whitelabled:Boolean= false,
    @SerializedName("tokens")  val tokens: Tokens= Tokens(),
    @SerializedName("reference")   val reference: Reference = Reference(),
    @SerializedName("customer")  val customer: Customer = Customer(),
    @SerializedName("customerExtraData") val customerExtraData :String="",
    @SerializedName("returnUrl")  val returnUrl: String ="",
    @SerializedName("cancelUrl")   val cancelUrl: String = "",
    @SerializedName("notificationUrl")  val notificationUrl: String = "",
    @SerializedName("notificationType")  val notificationType: String = "all",
    @SerializedName("isTest")  val isTest: Boolean = false,
    @SerializedName("sessionId")  val sessionId: String = "",
    @SerializedName("plugin")  val plugin: Plugin = Plugin()

) : Parcelable {

     fun getJson(): JSONObject? {
        return merge(UpaymentGatewayAppUtils.dataJson!!, APagination!!, mJProperties!!)
    }

    var mJProperties: JSONObject
     var mJPagination: JSONObject
    protected var event = ""
    var stringBuilder: StringBuilder? = null

    protected val APagination = JSONObject()

    init {
        mJProperties = JSONObject()
        mJPagination  = JSONObject()
//        if (!merchantId.startsWith("/")) {
//            this.event = merchantId
//            merchantId = "/$merchantId"
//        }
//        put(UpaymentGatewayAppUtils.dataJson, UpaymentGatewayEvent.KEY_MERCHANT_ID, merchantId)
      //  put(UpaymentGatewayAppUtils.dataJson, UpaymentGatewayAppUtils.PRODUCT_DATA_KEY, product)
//        put(UpaymentGatewayAppUtils.dataJson, UpaymentGatewayAppUtils.ORDER_DATA_KEY, order)
//        put(UpaymentGatewayAppUtils.dataJson, UpaymentGatewayAppUtils.PAYMENT_GATEWAY_KEY, paymentGateway)
        put(UpaymentGatewayAppUtils.dataJson, UpaymentGatewayAppUtils.LANGUAGE_KEY, language)
        put(UpaymentGatewayAppUtils.dataJson, UpaymentGatewayAppUtils.IS_SAVED_CARD_KEY, isSaveCard)
//        put(UpaymentGatewayAppUtils.dataJson, UpaymentGatewayAppUtils.TOKENS_KEY, tokens)
//        put(UpaymentGatewayAppUtils.dataJson, UpaymentGatewayAppUtils.REFERENCE_KEY, reference)
//        put(UpaymentGatewayAppUtils.dataJson, UpaymentGatewayAppUtils.CUSTOMER_KEY, customer)
        put(UpaymentGatewayAppUtils.dataJson, UpaymentGatewayAppUtils.notificationType, notificationType)
        put(UpaymentGatewayAppUtils.dataJson, UpaymentGatewayAppUtils.isTest, isTest)
        put(UpaymentGatewayAppUtils.dataJson, UpaymentGatewayAppUtils.customerExtraData, customerExtraData)

        put(UpaymentGatewayAppUtils.dataJson, UpaymentGatewayAppUtils.IS_Whitelabled, is_whitelabled)
        put(UpaymentGatewayAppUtils.dataJson, UpaymentGatewayAppUtils.sessionId, sessionId)


        put(UpaymentGatewayAppUtils.dataJson, UpaymentGatewayAppUtils.RETURN_URL_KEY, returnUrl)
        put(UpaymentGatewayAppUtils.dataJson, UpaymentGatewayAppUtils.CANCEL_URL_KEY, cancelUrl)
        put(UpaymentGatewayAppUtils.dataJson, UpaymentGatewayAppUtils.NOTIFICATION_URL_KEY, notificationUrl)


    }

}

@Parcelize
class Product(
    @SerializedName("title")   val title: List<String> = arrayListOf(),
    @SerializedName("name")   val name: List<String> = arrayListOf(),
    @SerializedName("price")   val price: List<Float> = arrayListOf(),
    @SerializedName("qty")    val qty: List<Int> = arrayListOf(),
) : Parcelable {

    init {
        var productName: String? = null
        var title: String? = null
        if (name != null && name.isNotEmpty()) {
            productName = name.joinToString()

        }
        var convertJsonString = Gson().toJson(this)
        val jsonObject = JSONObject(convertJsonString)

//        convertJsonString.replaceAll("\\","");


      //  put(UpaymentGatewayAppUtils.dataJson, UpaymentGatewayAppUtils.PRODUCT_DATA_KEY, jsonObject.toString())
        put(UpaymentGatewayAppUtils.dataJson, UpaymentGatewayAppUtils.PRODUCT_DATA_KEY, jsonObject)

    }
}
@Parcelize
class Order(
    @SerializedName("id")   val id: String="",
    @SerializedName("reference")  val reference: String="",
    @SerializedName("description")  val description: String="",
    @SerializedName("currency")  val currency: String="",
    @SerializedName("amount")  val amount: Float=0.0f,
) : Parcelable {

    init {
        var convertJsonString = Gson().toJson(this)
        val jsonObject = JSONObject(convertJsonString)
        put(UpaymentGatewayAppUtils.dataJson, UpaymentGatewayAppUtils.ORDER_DATA_KEY, jsonObject)

    }
}
@Parcelize
class PaymentGateway(
    @SerializedName("src") var src: String="",
) : Parcelable {
    init {
        var convertJsonString = Gson().toJson(this)
        val jsonObject = JSONObject(convertJsonString)
        put(UpaymentGatewayAppUtils.dataJson, UpaymentGatewayAppUtils.PAYMENT_GATEWAY_KEY, jsonObject)
    }
}
@Parcelize
class Tokens(
    @SerializedName("kFast")  val kFast: String = "",
    @SerializedName("creditCard")  val creditCard: String="",
    @SerializedName("customerUniqueToken")  val customerUniqueToken: String ="",
) : Parcelable {

    init {
        var convertJsonString = Gson().toJson(this)
        val jsonObject = JSONObject(convertJsonString)
        put(UpaymentGatewayAppUtils.dataJson, UpaymentGatewayAppUtils.TOKENS_KEY, jsonObject)
    }
}
@Parcelize
class Reference(
    @SerializedName("id") val id: String="",
) : Parcelable {
    init {
        var convertJsonString = Gson().toJson(this)
        val jsonObject = JSONObject(convertJsonString)
        put(UpaymentGatewayAppUtils.dataJson, UpaymentGatewayAppUtils.REFERENCE_KEY, jsonObject)
    }
}

@Parcelize
class Plugin(
    @SerializedName("src") val src: String="",
) : Parcelable {
    init {
        var convertJsonString = Gson().toJson(this)
        val jsonObject = JSONObject(convertJsonString)
        put(UpaymentGatewayAppUtils.dataJson, UpaymentGatewayAppUtils.plugin, jsonObject)
    }
}


@Parcelize
class Customer(
    @SerializedName("uniqueId")  val uniqueId: String ="",
    @SerializedName("name") val name: String = "",
    @SerializedName("email")  val email: String = "",
    @SerializedName("mobile") val mobile: String= "",
) : Parcelable {

    init {
        var convertJsonString = Gson().toJson(this)
        val jsonObject = JSONObject(convertJsonString)
        put(UpaymentGatewayAppUtils.dataJson, UpaymentGatewayAppUtils.CUSTOMER_KEY, jsonObject)
    }
}

