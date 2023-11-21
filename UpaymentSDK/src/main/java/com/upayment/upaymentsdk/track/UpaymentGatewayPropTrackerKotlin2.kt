package com.upayment.upaymentsdk.track

import android.annotation.SuppressLint
import android.os.Handler
import com.google.gson.Gson
import com.upayment.upaymentsdk.track.charge.UpaymentGatewayEvent3
import com.upayment.upaymentsdk.track.customertoken.PostCustomerToken
import com.upayment.upaymentsdk.util.PreferenceHelper
import com.upayment.upaymentsdk.util.PreferenceHelper.customerToken

class UpaymentGatewayPropTrackerKotlin2 (var properties: UpaymentGatewayEvent3,var  handler: Handler?) :Runnable {

    @SuppressLint("SuspiciousIndentation")
    override fun run() {
        handler!!.removeMessages(UpaymentGateway.HANDLER_MESSAGE_RETRY)
        var propertiesToString: String? = null
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

            val numberRandom = UpaymentGatewayConfig.generateNumber()
            dataPost.customerUniqueToken=  numberRandom.toString();

            val   dataSend = Gson().toJson(dataPost)
            var response_customerUniqueToken: String? =null
            if(customerToken==null || customerToken.isEmpty()  ){
                 response_customerUniqueToken=  HttpPostRequest.getcustomerUniqueToken(dataSend,properties.is_whitelabled)
            }else{
                response_customerUniqueToken=customerToken
            }
            if(response_customerUniqueToken.equals("error")){

            }
            if(response_customerUniqueToken!=null && !response_customerUniqueToken.equals("Error")){
                if (properties != null) {
                    properties.tokens.customerUniqueToken= response_customerUniqueToken
                    propertiesToString = Gson().toJson(properties)
                }
                val response=      HttpPostRequest.sendPostRequest(response_customerUniqueToken,propertiesToString,properties);
                if(  response!=null ){

                    UpaymentGatewayLog.d("-> api call charge!"+ response,true)
                }
            }
            return
        }
     // Todo Commented
        AdosizAnalyticsStoredPropTracker(handler).run() //
    }
}