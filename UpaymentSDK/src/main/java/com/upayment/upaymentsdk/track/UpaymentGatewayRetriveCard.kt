package com.upayment.upaymentsdk.track

import android.annotation.SuppressLint
import android.os.Handler
import com.google.gson.Gson
import com.upayment.upaymentsdk.track.customertoken.PostCustomerToken
import com.upayment.upaymentsdk.util.PreferenceHelper
import com.upayment.upaymentsdk.util.PreferenceHelper.customerToken

class UpaymentGatewayRetriveCard (
    var properties: String,
    var handler: Handler?,
    var is_whitelabled: Boolean
) :Runnable {

    @SuppressLint("SuspiciousIndentation")
    override fun run() {
        handler!!.removeMessages(UpaymentGateway.HANDLER_MESSAGE_RETRY)
        if (!UpaymentGatewayConnectionHelper.isConnected(UpaymentGateway.getContext())) {
            UpaymentGatewayLog.d("-> no network access. Properties is being stored and will be sent later.", true)
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



            var response_customerUniqueToken: String? =null
            if(customerToken==null || customerToken.isEmpty()  ){
                val numberRandom = UpaymentGatewayConfig.generateNumber()
                dataPost.customerUniqueToken=  numberRandom.toString();
                val   dataSend = Gson().toJson(dataPost)

                response_customerUniqueToken=  HttpPostRequest.getcustomerUniqueToken(dataSend,is_whitelabled)
            }else{
                response_customerUniqueToken=customerToken
            }
            if(response_customerUniqueToken!=null && !response_customerUniqueToken.equals("Error")){
                dataPost.customerUniqueToken=  response_customerUniqueToken.toString();
                val   dataSend = Gson().toJson(dataPost)
            val response=    HttpPostRequest.getRetriveCard(dataSend,false);
                if(response!=null){

                }

            }


            return
        }
        // Todo Commented
        AdosizAnalyticsStoredPropTracker(handler).run() //
    }
}