package com.upayment.upaymentsdk.track

import android.annotation.SuppressLint
import android.os.Handler
import com.google.gson.Gson
import com.upayment.upaymentsdk.track.customertoken.PostCustomerToken

class UpaymentGatewayCustomerUniqueToken(
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

        val response=    HttpPostRequest.getcustomerUniqueToken(properties,is_whitelabled)

            if(response!=null && !response.equals("error")){


            }else{
                val dataPost = PostCustomerToken()
                val numberRandom = UpaymentGatewayConfig.generateNumber()
                dataPost.customerUniqueToken = numberRandom.toString()

                val dataSend = Gson().toJson(dataPost)
                val response=    HttpPostRequest.getcustomerUniqueToken(dataSend,is_whitelabled)
                if(response!=null && !response.equals("error")){


                }else{

                }

                // again call token api  with different random number
            }



            return
        }
        // Todo Commented
        AdosizAnalyticsStoredPropTracker(handler).run() //
    }
}