package com.upayment.upaymentsdk.util

import android.content.Context
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import com.upayment.upaymentsdk.track.Product
import org.json.JSONObject

object UpaymentGatewayAppUtils {
  //  https://preprod-uinterface.upayments.com/api/v1/charge
    // https://dev-apiv2api.upayments.com

    const val KEY_SAVE_EVENT = "SENT_EVENT"
    const val KEY_HEADER_TOKEN = "header_token"

    const val ABC = "https://"

    // Production
    const val BASEURLLISTPAYMENT="https://apiv2api.upayments.com/"
    // dev
//    const val BASEURLLISTPAYMENT="https://dev-apiv2api.upayments.com/"

    const val BASEURL2="api/v1"

   // https://dev-apiv2api.upayments.com/api/v1/charge
//    const val NAME = "preprod-uinterface.upayments.com"
    const val NAME= "dev-apiv2api.upayments.com"

    @JvmField
    var EVENT_INSTALL = false
    @JvmField
    var refererUrlSave = ""

    //Eventx
    const val INSTALL = "install"
    const val ORGANIC = "organic"
    const val OPEN = "open"
    const val KEY_ORDER_VALUE = "order_value"

    // payment-request
    const val TGET = "api/v1/charge"
    const val TGET_LIST="check-payment-button-status"
    const val TGET_SAND = "api/v1/charge"
    const val QUERRY = "?"
    const val SLASH = "/"
    const val SDK_VERSION = 23

    const val TGET_UNIQUE_TOKEN="api/v1/create-customer-unique-token"

    const val TGET_RETRIVE_CARD="api/v1/retrieve-customer-cards"


    const val TPOST_SINGLE_REFUND="api/v1/create-refund"

    const val TPOST_MULTIPLE_REFUND="api/v1/create-multivendor-refund"

    const val TPOST_MULTIPLE_REFUND_DELETE="api/v1/delete-multivendor-refund"

    const val TPOST_ADD_CARDS="api/v1/create-token-from-card"




    const val TPOST_SINGLE_DELETE_REFUND="api/v1/delete-refund"


    const val KEY_MERCHANT_ID_KEY = "merchant_id"
    const val PRODUCT_DATA_KEY ="product"
    const val PRODUCT_TITLE_KEY ="title_product"
    const val PRODUCT_NAME_KEY ="product_name"

    const val ORDER_DATA_KEY="order"
    const val PAYMENT_GATEWAY_KEY ="paymentGateway"
    const val LANGUAGE_KEY="language"
    const val IS_SAVED_CARD_KEY="isSaveCard"
    const val TOKENS_KEY="tokens"
    const val REFERENCE_KEY="reference"
    const val CUSTOMER_KEY="customer"
    const val RETURN_URL_KEY="returnUrl"
    const val CANCEL_URL_KEY="cancelUrl"
    const val IS_Whitelabled="is_whitelabled"
    const val customerExtraData="customerExtraData"
    const val sessionId="sessionId"
    const val isTest="isTest"
    const val plugin="plugin"


    var notificationType="notificationType"
    const val NOTIFICATION_URL_KEY= "notificationUrl"
    const val SOURCE= "source="



    var dataJson = JSONObject()

    var productDataJson = JSONObject()





    /**
     * @return true If device has Android Marshmallow or above version
     */
    fun hasM(): Boolean {
        return VERSION.SDK_INT >= VERSION_CODES.M
    }

    fun isMSupportDevice(ctx: Context?): Boolean {
        return VERSION.SDK_INT >= SDK_VERSION
    }


}