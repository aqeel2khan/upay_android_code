package com.upayment.upaymentsdk.track

import android.os.Handler
import com.google.gson.Gson
import com.upayment.upaymentsdk.track.charge.UpaymentGatewayEvent3

class UpaymentGatewayPropTrackerKotlin(var properties: UpaymentGatewayEvent3,var  handler: Handler?) :Runnable {

//    private var handler: Handler? = null
//    private var properties: UpaymentGatewayEvent2? = null

//    this.properties = properties
//    handler = ui
//
//    fun UpaymentGatewayPropTrackerKotlin(properties: UpaymentGatewayEvent2?, ui: Handler?) {
//        this.properties = properties
//        handler = ui
//    }

    override fun run() {
        handler!!.removeMessages(UpaymentGateway.HANDLER_MESSAGE_RETRY)
        var propertiesToString: String? = null
        if (properties != null) {
                 propertiesToString = Gson().toJson(properties)

//            propertiesToString= properties!!.getJson().toString()

          //  propertiesToString = properties.json.toString()
        }
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

            val response=      HttpPostRequest.    sendPostRequest("",propertiesToString,properties);

                    //  val success = UpaymentGatewayHttpClient.postData("$propertiesToString", false)

            if(  response!=null){

            }

//            val success = UpaymentGatewayHttpClient.postData("[$propertiesToString]", false)
//            if (!success) {
//                UpaymentGatewayLog.d("-> synchronization failed. Will retry if no other pending track is found.")
//                UpaymentGatewayFileHelper.appendLine(propertiesToString)
//                if (propertiesToString == null) {
//                    return
//                }
//                handler!!.sendEmptyMessageDelayed(
//                    UpaymentGateway.HANDLER_MESSAGE_RETRY,
//                    UpaymentGatewayConfig.POST_FAILED_RETRY_DELAY_MILLIS
//                )
//            } else {
//                UpaymentGatewayLog.d("-> properties tracked !", true)
//            }
            return
        }

//        UpaymentGatewayLog.d("-> " + storedProperties.size() + " stored properties found, current properties added to history to " +
//                "be sent with stored ones.");
        //    UpaymentGatewayFileHelper.appendLine(propertiesToString); //we treat the current properties as a stored properties (history)
// Todo Commented
        AdosizAnalyticsStoredPropTracker(handler).run() //
    }
}