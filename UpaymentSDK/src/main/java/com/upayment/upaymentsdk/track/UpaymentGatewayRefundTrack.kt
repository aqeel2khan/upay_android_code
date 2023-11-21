package com.upayment.upaymentsdk.track

import android.annotation.SuppressLint
import android.os.Handler
import com.google.gson.Gson
import com.upayment.upaymentsdk.UPaymentCallBack
import com.upayment.upaymentsdk.track.customertoken.PostCustomerToken
import com.upayment.upaymentsdk.track.refund.PostMultiRefundData
import com.upayment.upaymentsdk.track.refund.PostSingleRefundData
import com.upayment.upaymentsdk.track.refund.PostSingleRefundDataApi
import com.upayment.upaymentsdk.track.refund.RefundPayload
import com.upayment.upaymentsdk.track.refund.ResponseMultivendorRefundData
import com.upayment.upaymentsdk.track.refund.multirefund.ResponseMultivendorRefund
import com.upayment.upaymentsdk.util.PreferenceHelper
import com.upayment.upaymentsdk.util.PreferenceHelper.customerToken
import org.json.JSONObject

class UpaymentGatewayRefundTrack(
    var properties: PostSingleRefundData,
    var handler: Handler?,
    var is_whitelabled: Boolean
) :Runnable {

    private var isMultivendorRefund: Boolean= false

    @SuppressLint("SuspiciousIndentation")
    override fun run() {
        handler!!.removeMessages(UpaymentGateway.HANDLER_MESSAGE_RETRY)
        var propertiesToString: String = ""
        if (!UpaymentGatewayConnectionHelper.isConnected(UpaymentGateway.getContext())) {
            UpaymentGatewayLog.d("-> no network access. Properties is being stored and will be sent later.", true)
            // UpaymentGatewayFileHelper.appendLine(propertiesToString); //ToDO Commented dont store to resend
            handler!!.sendEmptyMessageDelayed(
                UpaymentGateway.HANDLER_MESSAGE_RETRY,
                UpaymentGatewayConfig.NO_INTERNET_RETRY_DELAY_MILLIS
            )
            return
        }
        val storedProperties = UpaymentGatewayFileHelper.getLines()
        if (storedProperties.isEmpty()) {
//            val customerToken=    PreferenceHelper.customPreference(context = UpaymentGateway.getContext()  ,"customer_unique_token").customerToken

            if (properties != null) {
                val postDataAPI = PostSingleRefundDataApi()
                postDataAPI.customerEmail = properties.customerEmail
                postDataAPI.customerFirstName = properties.customerEmail
                postDataAPI.orderId = properties.orderId
                postDataAPI.notifyUrl = properties.notifyUrl
                postDataAPI.reference = properties.reference
                postDataAPI.customerMobileNumber = properties.customerMobileNumber
                postDataAPI.totalPrice = properties.totalPrice

                propertiesToString = Gson().toJson(postDataAPI)
            }
            val response = HttpPostRequest.postSingleRefund(propertiesToString, is_whitelabled, properties);
            if(response!=null && !response.isEmpty()){
                val jsonObjectData = JSONObject(response)

                        if(jsonObjectData!=null){
                            if(jsonObjectData.has("isMultivendorRefund")){
                                isMultivendorRefund =    jsonObjectData.getBoolean("isMultivendorRefund")
                                if( jsonObjectData.has("refundPayload")){

                                    UpaymentGatewayLog.d("-> api call Refund Response!" + response, true)
                                    val gson = Gson()
                                    val responseMultivendorRefundData: ResponseMultivendorRefundData =
                                        gson.fromJson(response.toString(), ResponseMultivendorRefundData::class.java)
                                    if (responseMultivendorRefundData != null) {

                                        val userPostPayload = properties.refundPayload
                                        if (userPostPayload != null && userPostPayload.size > 0) {

                                            for (userpostData in userPostPayload) {
                                                if (userpostData != null) {

                                                    var listData: List<RefundPayload>? =
                                                        responseMultivendorRefundData?.refundPayload?.filter { it ->
                                                            it.ibanNumber.equals(userpostData.ibanNumber)
                                                        }
                                                    listData?.get(0)?.amountToRefund = userpostData.amountToRefund

//                                    val  refundPayloadData: RefundPayload = responseMultivendorRefundData.refundPayload?.filter { refundPayload -> refundPayload.ibanNumber.equals(userpostData.ibanNumber) }
//                                ?.single() ?: RefundPayload()
                                                }
                                            }
                                            val postMultivendorRefundData = PostMultiRefundData()
                                            postMultivendorRefundData.refundPayload = responseMultivendorRefundData?.refundPayload!!
                                            postMultivendorRefundData.customerEmail = properties.customerEmail
                                            postMultivendorRefundData.customerFirstName = properties.customerEmail
                                            postMultivendorRefundData.orderId = properties.orderId
                                            postMultivendorRefundData.notifyUrl = properties.notifyUrl
                                            postMultivendorRefundData.reference = properties.reference
                                            postMultivendorRefundData.customerMobileNumber = properties.customerMobileNumber

                                            val propertiesToPostMultiRefund = Gson().toJson(postMultivendorRefundData)

                                            // call MultiRefundAPI from here
                                            val responseMultiRefund = HttpPostRequest.postMultiRefund(
                                                propertiesToPostMultiRefund,
                                                is_whitelabled,
                                                postMultivendorRefundData
                                            );
                                            if (responseMultiRefund != null) {

                                                val gson = Gson()
                                                val responseMultivendorRefund: ResponseMultivendorRefund =
                                                    gson.fromJson(responseMultiRefund.toString(), ResponseMultivendorRefund::class.java)
                                                if (responseMultivendorRefund != null) {
                                                    val uPaymentCallBack = UpaymentGateway.UPaymentCallBack
                                                    uPaymentCallBack.sucessMultiRefund(responseMultivendorRefund)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }



            }else{

                UpaymentGatewayLog.d("-> api call Refund Response Error!" + response, true)

            }

            return

            // Todo Commented
            AdosizAnalyticsStoredPropTracker(handler).run() //
        }
    }
}