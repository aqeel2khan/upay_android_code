package com.upayment.upaymentsdk.track

import android.os.Handler
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class AdosizAnalyticsStoredPropTrackerKotlin(uiHandler: Handler) : Runnable {

    var handler: Handler? = null

//    fun AdosizAnalyticsStoredPropTracker(ui: Handler) {
//        this.handler = ui
//    }

    override fun run() {
        UpaymentGatewayLog.d("Tracking stored properties")
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
            UpaymentGatewayLog.d("-> no properties stored.")
            return
        }
        UpaymentGatewayLog.d("-> " + storedProperties.size + " stored properties found. Added for synchronization.")
        val result: Int = postStoredProperties(storedProperties)
        if (result == -1) {
            handler!!.sendEmptyMessageDelayed(
                UpaymentGateway.HANDLER_MESSAGE_RETRY,
                UpaymentGatewayConfig.POST_FAILED_RETRY_DELAY_MILLIS
            )
        } else {
            UpaymentGatewayLog.d("-> all properties tracked successfully. Stored properties is now empty.")
        }
    }

    /**
     * @param storedProperties
     * @return the number of properties that have been synchronized. -1 otherwise
     */
    fun postStoredProperties(storedProperties: List<String>): Int {
        var counter = 0
        var jsonArray = JSONArray()
        do {
            if (counter == storedProperties.size) {
                return counter
            } else if (counter < storedProperties.size) {
                val line = storedProperties[counter]
                UpaymentGatewayLog.d("-> counter position : $counter")
                try {
                    val lineJson = JSONObject(line)
                    jsonArray.put(lineJson)

//                if (counter == storedProperties.size() - 1 // last item
//                        || (jsonArray.toString().getBytes().length + storedProperties.get(counter + 1)
//                        .getBytes().length) > UpaymentGatewayConfig.MAX_UNZIPPED_BYTES_PER_SEND) {

                    // no more data OR json array is becoming too big -> send it
                    val success = UpaymentGatewayHttpClient.postData(jsonArray.toString(), true)
                    if (success) {
                        UpaymentGatewayFileHelper.deleteLines(jsonArray.length())
                        jsonArray = JSONArray() // re-init in case the is still pending data.
                        UpaymentGatewayLog.d("-> properties tracked !", true)
                    } else {
                        // something went wrong, will try on track() next call. This avoid infinite loop.
                        UpaymentGatewayLog.d("-> synchronization failed. Will retry later.")
                        UpaymentGatewayLog.d("-> failed post$jsonArray")
                        UpaymentGatewayFileHelper.deleteLines(jsonArray.length())

                        // Todo BY ADIL FOR NOW TESTING I AM DELETING
                        //  UpaymentGatewayFileHelper.deleteLines(jsonArray.length()-1);
                        UpaymentGatewayLog.d("-> list position : $counter")
                        jsonArray = JSONArray()

                        //ToDo
                        // Commented by Adil so that it may move to next value
                        // return -1;
                    }
                    //   }
                } catch (e: JSONException) {
                    UpaymentGatewayLog.e("Failed to JSON encode properties : $line. Exception: $e")
                }
                UpaymentGatewayLog.d("-> counter number $counter")
            }
            counter++
        } while (counter < storedProperties.size)
        return counter
    }
}