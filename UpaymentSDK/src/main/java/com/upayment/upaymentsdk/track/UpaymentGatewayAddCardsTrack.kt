package com.upayment.upaymentsdk.track

import android.annotation.SuppressLint
import android.os.Handler
import com.google.gson.Gson
import com.upayment.upaymentsdk.track.card.Customer
import com.upayment.upaymentsdk.track.customertoken.PostCustomerToken
import com.upayment.upaymentsdk.util.PreferenceHelper
import com.upayment.upaymentsdk.util.PreferenceHelper.customerToken

class UpaymentGatewayAddCardsTrack (
    var properties: Customer,
    var handler: Handler?,
    var is_whitelabled: Boolean
) :Runnable {

    @SuppressLint("SuspiciousIndentation")
    override fun run() {
        handler!!.removeMessages(UpaymentGateway.HANDLER_MESSAGE_RETRY)
        var propertiesToString: String =""
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
            val customerToken=    PreferenceHelper.customPreference(context = UpaymentGateway.getContext()  ,"customer_unique_token").customerToken
            val dataPost  = PostCustomerToken()
         val numberRandom=   UpaymentGatewayConfig.generateNumber();
            dataPost.customerUniqueToken=  numberRandom.toString();

            val   dataSend = Gson().toJson(dataPost)
            var response_customerUniqueToken: String? =""
            if(customerToken==null || customerToken.isEmpty()  ){
                  response_customerUniqueToken=  HttpPostRequest.getcustomerUniqueToken(dataSend,is_whitelabled)
            }else{
                  response_customerUniqueToken=customerToken
            }
            if (customerToken != null) {
                properties.customerUniqueToken = customerToken.toLong()
            }
            if(response_customerUniqueToken!=null){
                if (properties != null) {

                    propertiesToString = Gson().toJson(properties)
                }
                val response=      HttpPostRequest.postAddCards(propertiesToString,is_whitelabled,properties);
                if(  response!=null ){
                    UpaymentGatewayLog.d("-> api call Add Cards!"+ response,true)
                }
            }
            return
        }
        // Todo Commented
        AdosizAnalyticsStoredPropTracker(handler).run() //
    }
}