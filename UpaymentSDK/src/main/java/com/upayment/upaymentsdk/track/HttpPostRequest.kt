package com.upayment.upaymentsdk.track
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import com.google.gson.Gson
import com.upayment.upaymentsdk.activity.ActivityWeb
import com.upayment.upaymentsdk.track.card.Customer
import com.upayment.upaymentsdk.track.card.addcard.ResponseAddCard
import com.upayment.upaymentsdk.track.card.retrive.ResponseRetriveCard
import com.upayment.upaymentsdk.track.charge.UpaymentGatewayEvent3
import com.upayment.upaymentsdk.track.customertoken.PostCustomerToken
import com.upayment.upaymentsdk.track.refund.PostDeleteSingleRefundData
import com.upayment.upaymentsdk.track.refund.PostMultiRefundData
import com.upayment.upaymentsdk.track.refund.PostSingleRefundData
import com.upayment.upaymentsdk.track.refund.SingleDeleteRefundResponse
import com.upayment.upaymentsdk.track.refund.SingleRefundResponse
import com.upayment.upaymentsdk.track.refund.delete.PostMultivendorRefundDelete
import com.upayment.upaymentsdk.track.refund.multidelete.ResponseMultiRefundDelete
import com.upayment.upaymentsdk.util.PreferenceHelper
import com.upayment.upaymentsdk.util.PreferenceHelper.customerToken
import com.upayment.upaymentsdk.util.UpaymentGatewayAppUtils
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class HttpPostRequest {
    companion object {
        fun sendPostRequest(customerUniqueToken: String, postData: String?, properties: UpaymentGatewayEvent3): String {
            var response = ""
            try {
                var urlTest:URL ?= null
                var url: URL? = null
                URLEncoder.encode(
                    UpaymentGatewayAppUtils.BASEURLLISTPAYMENT + UpaymentGatewayAppUtils.TGET

                )
                if (properties.is_whitelabled)
                    url =
                        URL(UpaymentGatewayAppUtils.BASEURLLISTPAYMENT + UpaymentGatewayAppUtils.TGET)

                else {
                    url =
                        URL(UpaymentGatewayAppUtils.BASEURLLISTPAYMENT + UpaymentGatewayAppUtils.TGET)

                }
                val conn = url!!.openConnection() as HttpURLConnection
                // Set the request method to POST
                conn.requestMethod = "POST"

                // Enable output and input streams
                conn.doOutput = true
                conn.doInput = true

                var auth_header_token= UpaymentGateway.sdk_api_key
                if(auth_header_token.isNullOrEmpty()){
                    auth_header_token = UpaymentGateway.upaymentGatewayAppPreferences.getString(
                        UpaymentGatewayAppUtils.KEY_HEADER_TOKEN,
                        ""
                    )

                }
                UpaymentGatewayLog.d("-> Charge API Request !"+ postData.toString(),true)

                //oxxnDz0ES48qyaT96f8VG6YYyFr0krk2akJI7LH5
                conn.setRequestProperty("Authorization", "Bearer " + auth_header_token);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                conn.setRequestProperty("Accept", "application/json")
                conn.setRequestProperty("charset", "utf-8")

                //  conn.setRequestProperty("Content-Type", "application/json");


                // Write the POST data to the connection's output stream
                val outputStream: OutputStream = conn.outputStream
                val writer = BufferedWriter(OutputStreamWriter(outputStream, "UTF-8"))
                writer.write(postData.toString())
                writer.flush()
                writer.close()
                outputStream.close()

                // Get the response from the server
                val responseCode = conn.responseCode
                UpaymentGatewayLog.d("-> Charge API Status !"+ responseCode.toString(),true)

                if (responseCode == HttpURLConnection.HTTP_OK || responseCode==HttpURLConnection.HTTP_CREATED) {
                    val inputStream = BufferedReader(InputStreamReader(conn.inputStream))
                    val responseStringBuilder = StringBuilder()
                    var line: String?
                    while (inputStream.readLine().also { line = it } != null) {
                        responseStringBuilder.append(line).append("\n")
                    }
                    inputStream.close()
                    response = responseStringBuilder.toString()
                    UpaymentGatewayLog.d("-> Charge API Response !"+ response.toString(),true)

                } else {
                    response = "Error"
                    UpaymentGatewayLog.d("-> Charge API Error !"+ response.toString(),true)

                }
                conn.disconnect()

                val jsonObject = JSONObject(response)
                UpaymentGatewayLog.d("-> Charge API Response !"+ jsonObject.toString(),true)

                var status =false
                if(jsonObject.has("status")){
                    status  = jsonObject.getBoolean("status")
                }
                if (status) {
                    try {
                        try {
                            // If the response is a JSON object
                            // Process the JSON data here as needed
                            val messageData = jsonObject.getString("message")
                            if(messageData!=null && !TextUtils.isEmpty(messageData)){

                            }
                            val jsonObjectData= jsonObject.getJSONObject("data")
                            if(jsonObjectData !=null){

                                if(properties.paymentGateway.src.equals("create-invoice")){

                                    val sms =    jsonObjectData.getBoolean("sms")
                                    val email =    jsonObjectData.getBoolean("email")
                                    val link =    jsonObjectData.getBoolean("link")
                                    val url =    jsonObjectData.getString("url")

                                val invoiceData:    CreateInvoiceResponse= CreateInvoiceResponse()
                                    invoiceData.status = true
                                    invoiceData.message =  messageData
                                    invoiceData.sms = sms
                                    invoiceData.email =  email
                                    invoiceData.link =  link
                                    invoiceData.url =  url

                                    val uPaymentCallBack = UpaymentGateway.UPaymentCallBack
                                    uPaymentCallBack.sucessCreateInvoice(invoiceData)
                                    return response

                                }else {

                                    val urlLoad =    jsonObjectData.getString("link")
                                    if(urlLoad!=null){

                                        Handler(Looper.getMainLooper()).post {
                                            val intentSend = Intent(UpaymentGateway.mAppContext, ActivityWeb::class.java)
                                            intentSend.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                            intentSend.putExtra("postData", urlLoad)
                                            intentSend.putExtra("successUrl", properties.returnUrl)
                                            intentSend.putExtra("errorUrl", properties.cancelUrl)
                                            intentSend.putExtra("notifyUrl", properties.notificationUrl)
                                            UpaymentGateway.mAppContext.startActivity(intentSend)
                                        }
                                    }
                                }


                            }
                        } catch (e: Exception) {
                            // Handle any parsing or processing errors here
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                } else {
                    UpaymentGatewayLog.e("postData failed : " + "Error")
                    val uPaymentCallBack = UpaymentGateway.UPaymentCallBack
                    uPaymentCallBack.errorPayUpayment("Error on Getting paymentURL")
                    return "error"
                }

            } catch (e: Exception) {
                response = "Error"
            }
            return response
        }
        fun sendPostRequestList(source: String, postData: String, properties: UpaymentGatewayEvent3): String {
            var response = ""
            try {
                var url: URL? = null


                URLEncoder.encode(
                    UpaymentGatewayAppUtils.BASEURLLISTPAYMENT + UpaymentGatewayAppUtils.BASEURL2 + UpaymentGatewayAppUtils.SLASH +UpaymentGatewayAppUtils.TGET_LIST+UpaymentGatewayAppUtils.QUERRY+UpaymentGatewayAppUtils.SOURCE+source

                )
                if (properties.is_whitelabled)
                    url =
                        URL(UpaymentGatewayAppUtils.BASEURLLISTPAYMENT + UpaymentGatewayAppUtils.BASEURL2 + UpaymentGatewayAppUtils.SLASH +UpaymentGatewayAppUtils.TGET_LIST+UpaymentGatewayAppUtils.QUERRY+UpaymentGatewayAppUtils.SOURCE+source)

                else {
                    url =
                        URL(UpaymentGatewayAppUtils.BASEURLLISTPAYMENT + UpaymentGatewayAppUtils.BASEURL2 + UpaymentGatewayAppUtils.SLASH +UpaymentGatewayAppUtils.TGET_LIST +UpaymentGatewayAppUtils.QUERRY+UpaymentGatewayAppUtils.SOURCE+source)

                }

             //   url = URL("https://apiv2api.upayments.com/api/v1/check-payment-button-status?source=sdk")




                var auth_header_token= UpaymentGateway.sdk_api_key
                if(auth_header_token.isNullOrEmpty()){
                    auth_header_token = UpaymentGateway.upaymentGatewayAppPreferences.getString(
                        UpaymentGatewayAppUtils.KEY_HEADER_TOKEN,
                        ""
                    )

                }

                //

                val client = OkHttpClient()

                val request = Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer $auth_header_token")
                    .build()

                val response = client.newCall(request).execute()

                val responseCode = response.code


                return if (response.isSuccessful) {
                    response.body?.string() ?: "Empty"
                } else {
                    "Request failed with response code ${response.code}"
                }

                UpaymentGatewayLog.d("-> Payment List API Post !"+ postData.toString(),true)

                // Get the response from the server
              //  val responseCode = conn.co
                UpaymentGatewayLog.d("-> Payment List  API Status !"+ responseCode.toString(),true)

                if (responseCode == HttpURLConnection.HTTP_OK || responseCode==HttpURLConnection.HTTP_CREATED) {

                 //   response = responseStringBuilder.toString()
                    UpaymentGatewayLog.d("->  Success Payment  List api   API!"+ response.toString(),true)

                } else {
                   // response = "Error"
                    UpaymentGatewayLog.d("-> Error call Payment List API!"+ response.toString(),true)

                }

            } catch (e: Exception) {
                response = "Error"
            }
            return response
        }

        fun postSingleRefund(postData: String, isWhitelabled: Boolean, properties: PostSingleRefundData):String {

            var response = ""
            try {

                val objData=  JSONObject()
                var urlTest:URL ?= null
                var url: URL? = null
                URLEncoder.encode(
                    UpaymentGatewayAppUtils.ABC + UpaymentGatewayAppUtils.NAME + UpaymentGatewayAppUtils.SLASH + UpaymentGatewayAppUtils.TPOST_SINGLE_REFUND

                )
                if (isWhitelabled)
                    url =
                        URL(UpaymentGatewayAppUtils.ABC + UpaymentGatewayAppUtils.NAME + UpaymentGatewayAppUtils.SLASH + UpaymentGatewayAppUtils.TPOST_SINGLE_REFUND)

                else {
                    url =
                        URL(UpaymentGatewayAppUtils.ABC + UpaymentGatewayAppUtils.NAME + UpaymentGatewayAppUtils.SLASH + UpaymentGatewayAppUtils.TPOST_SINGLE_REFUND)

                }
                val conn = url!!.openConnection() as HttpURLConnection
                // Set the request method to POST
                conn.requestMethod = "POST"

                // Enable output and input streams
                conn.doOutput = true
                conn.doInput = true

                var auth_header_token= UpaymentGateway.sdk_api_key
                if(auth_header_token.isNullOrEmpty()){
                    auth_header_token = UpaymentGateway.upaymentGatewayAppPreferences.getString(
                        UpaymentGatewayAppUtils.KEY_HEADER_TOKEN,
                        ""
                    )

                }

                //Todo Comment
                //oxxnDz0ES48qyaT96f8VG6YYyFr0krk2akJI7LH5
                conn.setRequestProperty("Authorization", "Bearer " + auth_header_token);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                conn.setRequestProperty("Accept", "application/json")
                conn.setRequestProperty("charset", "utf-8")

                UpaymentGatewayLog.d("-> api call SingleRefund Post!"+ postData.toString(),true)


                // Write the POST data to the connection's output stream
                val outputStream: OutputStream = conn.outputStream
                val writer = BufferedWriter(OutputStreamWriter(outputStream, "UTF-8"))
                writer.write(postData.toString())
                writer.flush()
                writer.close()
                outputStream.close()

                // Get the response from the server
                val responseCode = conn.responseCode
                UpaymentGatewayLog.d("-> api call SingleRefund Status!"+ responseCode,true)

                if (responseCode == 201) {
                    val inputStream = BufferedReader(InputStreamReader(conn.inputStream))
                    val responseStringBuilder = StringBuilder()
                    var line: String?
                    while (inputStream.readLine().also { line = it } != null) {
                        responseStringBuilder.append(line).append("\n")
                    }
                    inputStream.close()
                    conn.disconnect()

                    response = responseStringBuilder.toString()
                    UpaymentGatewayLog.d("-> api call SingleRefund Response!"+ response,true)

                    val jsonObject = JSONObject(response)
                    var status =false
                    if(jsonObject.has("status")){
                        status  = jsonObject.getBoolean("status")
                    }
                    if (status) {
                        try {
                            try {
                                // If the response is a JSON object
                                // Process the JSON data here as needed
                                val messageData = jsonObject.getString("message")
                                var isMultivendorRefund =false
                                if(messageData!=null && !TextUtils.isEmpty(messageData)){

                                }
                                val jsonObjectData= jsonObject.getJSONObject("data")
                                if(jsonObjectData !=null){
                                    val orderId =    jsonObjectData.getString("orderId")
                                    //isMultivendorRefund
                                    if(jsonObjectData.has("isMultivendorRefund")){
                                        isMultivendorRefund =    jsonObjectData.getBoolean("isMultivendorRefund")
                                        if( jsonObjectData.has("refundPayload")){

                                            return jsonObjectData.toString()

                                        }
                                    }
                                    if(!isMultivendorRefund){
                                        val refundOrderId =    jsonObjectData.getString("refundOrderId")
                                        val refundArn =    jsonObjectData.getString("refundArn")
                                        val postback =  SingleRefundResponse()
                                        postback.status= status
                                        postback.message= messageData
                                        postback.orderId =  orderId
                                        postback.refundOrderId =  refundOrderId
                                        postback.refundArn =  refundArn
                                        val uPaymentCallBack = UpaymentGateway.UPaymentCallBack
                                        uPaymentCallBack.sucessSingleRefund(postback)

                                    }
                                    if(isMultivendorRefund ){
                                        return response
                                    }
                                }
                            } catch (e: Exception) {
                                // Handle any parsing or processing errors here
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        UpaymentGatewayLog.e("postData SingleRefund Failed : " + "Error")
                        val messageData = jsonObject.getString("message")
                        val uPaymentCallBack = UpaymentGateway.UPaymentCallBack
                        uPaymentCallBack.errorPayUpayment(messageData)
                        return response
                    }

                } else {

                    conn.disconnect()

                    response = ""
                    UpaymentGatewayLog.d("-> api call SingleRefund Error Response!"+ response,true)

                    UpaymentGatewayLog.e("postData SingleRefund Failed : " + "Error")
                    val uPaymentCallBack = UpaymentGateway.UPaymentCallBack
                    uPaymentCallBack.errorPayUpayment("Error")
                    return response

                }

            } catch (e: Exception) {
                response = response
            }
            return response
        }

        fun postDeleteSingleRefund(postData: String, isWhitelabled: Boolean, properties: PostDeleteSingleRefundData):String {

            var response = ""
            try {

                val objData=  JSONObject()
                var urlTest:URL ?= null
                var url: URL? = null
                URLEncoder.encode(
                    UpaymentGatewayAppUtils.ABC + UpaymentGatewayAppUtils.NAME + UpaymentGatewayAppUtils.SLASH + UpaymentGatewayAppUtils.TPOST_SINGLE_DELETE_REFUND

                )
                if (isWhitelabled)
                    url =
                        URL(UpaymentGatewayAppUtils.ABC + UpaymentGatewayAppUtils.NAME + UpaymentGatewayAppUtils.SLASH + UpaymentGatewayAppUtils.TPOST_SINGLE_DELETE_REFUND)

                else {
                    url =
                        URL(UpaymentGatewayAppUtils.ABC + UpaymentGatewayAppUtils.NAME + UpaymentGatewayAppUtils.SLASH + UpaymentGatewayAppUtils.TPOST_SINGLE_DELETE_REFUND)

                }
                val conn = url!!.openConnection() as HttpURLConnection
                // Set the request method to POST
                conn.requestMethod = "POST"

                // Enable output and input streams
                conn.doOutput = true
                conn.doInput = true

                var auth_header_token= UpaymentGateway.sdk_api_key
                if(auth_header_token.isNullOrEmpty()){
                    auth_header_token = UpaymentGateway.upaymentGatewayAppPreferences.getString(
                        UpaymentGatewayAppUtils.KEY_HEADER_TOKEN,
                        ""
                    )

                }

                //oxxnDz0ES48qyaT96f8VG6YYyFr0krk2akJI7LH5
                conn.setRequestProperty("Authorization", "Bearer " + auth_header_token);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                conn.setRequestProperty("Accept", "application/json")
                conn.setRequestProperty("charset", "utf-8")

                // Write the POST data to the connection's output stream
                val outputStream: OutputStream = conn.outputStream
                val writer = BufferedWriter(OutputStreamWriter(outputStream, "UTF-8"))
                writer.write(postData.toString())
                writer.flush()
                writer.close()
                outputStream.close()
                UpaymentGatewayLog.d("-> Single Delete API Post Data !"+ postData.toString(),true)


                // Get the response from the server
                val responseCode = conn.responseCode
                UpaymentGatewayLog.d("-> Single Delete API Status !"+ responseCode.toString(),true)

                if (responseCode == 201) {
                    val inputStream = BufferedReader(InputStreamReader(conn.inputStream))
                    val responseStringBuilder = StringBuilder()
                    var line: String?
                    while (inputStream.readLine().also { line = it } != null) {
                        responseStringBuilder.append(line).append("\n")
                    }
                    inputStream.close()
                    response = responseStringBuilder.toString()
                    UpaymentGatewayLog.d("-> Single Delete API Success !"+ response.toString(),true)

                } else {
                    response = "Error"
                    UpaymentGatewayLog.d("-> Single Delete API Error !"+ response.toString(),true)

                }
                conn.disconnect()
              

                val jsonObject = JSONObject(response)
                var status =false
                if(jsonObject!=null && jsonObject.has("status")){
                    status  = jsonObject.getBoolean("status")
                }
                if (status) {
                    try {
                        try {
                            // If the response is a JSON object
                            // Process the JSON data here as needed
                            val messageData = jsonObject.getString("message")
                            if(messageData!=null && !TextUtils.isEmpty(messageData)){

                            }
                            val jsonObjectData= jsonObject.getJSONObject("data")
                            if(jsonObjectData !=null){
                                val orderId =    jsonObjectData.getString("order_id")
                                val refundOrderId =    jsonObjectData.getString("refund_order_id")

                                val postback =  SingleDeleteRefundResponse()
                                postback.status= status
                                postback.message= messageData
                                postback.order_id =  orderId
                                postback.refund_order_id =  refundOrderId
                                val uPaymentCallBack = UpaymentGateway.UPaymentCallBack
                                uPaymentCallBack.sucessSingleDeleteRefund(postback)

                                if(orderId!=null){

                                    return orderId
                                }
                            }
                        } catch (e: Exception) {
                            // Handle any parsing or processing errors here
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                } else {
                    UpaymentGatewayLog.e("postData SingleRefund Failed : " + "Error")
                    var messageData ="Error";
                    if(jsonObject!=null && jsonObject.has("jsonObject")){
                         messageData = jsonObject.getString("jsonObject")
                    }

                    val uPaymentCallBack = UpaymentGateway.UPaymentCallBack
                    uPaymentCallBack.errorPayUpayment(messageData)
                    return "error"
                }

            } catch (e: Exception) {
                response = "Error"
            }
            return response
        }

        fun postMultiRefund(postData: String, isWhitelabled: Boolean, properties: PostMultiRefundData):String {

            var response = ""
            try {

                val objData=  JSONObject()
                var urlTest:URL ?= null
                var url: URL? = null
                URLEncoder.encode(
                    UpaymentGatewayAppUtils.ABC + UpaymentGatewayAppUtils.NAME + UpaymentGatewayAppUtils.SLASH + UpaymentGatewayAppUtils.TPOST_MULTIPLE_REFUND

                )
                if (isWhitelabled)
                    url =
                        URL(UpaymentGatewayAppUtils.ABC + UpaymentGatewayAppUtils.NAME + UpaymentGatewayAppUtils.SLASH + UpaymentGatewayAppUtils.TPOST_MULTIPLE_REFUND)

                else {
                    url =
                        URL(UpaymentGatewayAppUtils.ABC + UpaymentGatewayAppUtils.NAME + UpaymentGatewayAppUtils.SLASH + UpaymentGatewayAppUtils.TPOST_MULTIPLE_REFUND)

                }
                val conn = url!!.openConnection() as HttpURLConnection
                // Set the request method to POST
                conn.requestMethod = "POST"

                // Enable output and input streams
                conn.doOutput = true
                conn.doInput = true

                var auth_header_token= UpaymentGateway.sdk_api_key
                if(auth_header_token.isNullOrEmpty()){
                    auth_header_token = UpaymentGateway.upaymentGatewayAppPreferences.getString(
                        UpaymentGatewayAppUtils.KEY_HEADER_TOKEN,
                        ""
                    )

                }

                //oxxnDz0ES48qyaT96f8VG6YYyFr0krk2akJI7LH5
                conn.setRequestProperty("Authorization", "Bearer " + auth_header_token);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                conn.setRequestProperty("Accept", "application/json")
                conn.setRequestProperty("charset", "utf-8")

                // Write the POST data to the connection's output stream
                val outputStream: OutputStream = conn.outputStream
                val writer = BufferedWriter(OutputStreamWriter(outputStream, "UTF-8"))
                writer.write(postData.toString())
                writer.flush()
                writer.close()
                outputStream.close()
                UpaymentGatewayLog.d("-> Multi Refund API Post data !"+ postData.toString(),true)


                // Get the response from the server
                val responseCode = conn.responseCode
                UpaymentGatewayLog.d("-> Multi Refund API Success !"+ responseCode.toString(),true)
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode==HttpURLConnection.HTTP_CREATED) {
                    val inputStream = BufferedReader(InputStreamReader(conn.inputStream))
                    val responseStringBuilder = StringBuilder()
                    var line: String?
                    while (inputStream.readLine().also { line = it } != null) {
                        responseStringBuilder.append(line).append("\n")
                    }
                    inputStream.close()
                    response = responseStringBuilder.toString()
                    UpaymentGatewayLog.d("-> Multi Refund API Success !"+ response.toString(),true)

                } else {
                    response = "Error"
                    UpaymentGatewayLog.d("-> Multi Refund API Error !"+ response.toString(),true)


                }
                conn.disconnect()

                val jsonObject = JSONObject(response)
                var status =false
                if(jsonObject.has("status")){
                    status  = jsonObject.getBoolean("status")
                }
                if (status) {
                    try {
                        try {
                            // If the response is a JSON object
                            // Process the JSON data here as needed
                            val messageData = jsonObject.getString("message")
                            var isMultivendorRefund= 0
                            if(messageData!=null && !TextUtils.isEmpty(messageData)){

                            }
                            val jsonObjectData= jsonObject.getJSONObject("data")
                            if(jsonObjectData !=null){

                                // send call back from here to Activity
                              return  jsonObject.toString()

                            }
                        } catch (e: Exception) {
                            // Handle any parsing or processing errors here
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                } else {
                    UpaymentGatewayLog.e("postData SingleRefund Failed : " + "Error")
                    val messageData = jsonObject.getString("message")
                    val uPaymentCallBack = UpaymentGateway.UPaymentCallBack
                    uPaymentCallBack.errorPayUpayment(messageData)
                    return "error"
                }

            } catch (e: Exception) {
                response = "Error"
            }
            return response
        }

        fun postDeleteMultiRefund(postData: String, isWhitelabled: Boolean, properties: PostMultivendorRefundDelete):String {

            var response = ""
            try {

                val objData=  JSONObject()
                var urlTest:URL ?= null
                var url: URL? = null
                URLEncoder.encode(
                    UpaymentGatewayAppUtils.ABC + UpaymentGatewayAppUtils.NAME + UpaymentGatewayAppUtils.SLASH + UpaymentGatewayAppUtils.TPOST_MULTIPLE_REFUND_DELETE

                )
                if (isWhitelabled)
                    url =
                        URL(UpaymentGatewayAppUtils.ABC + UpaymentGatewayAppUtils.NAME + UpaymentGatewayAppUtils.SLASH + UpaymentGatewayAppUtils.TPOST_MULTIPLE_REFUND_DELETE)

                else {
                    url =
                        URL(UpaymentGatewayAppUtils.ABC + UpaymentGatewayAppUtils.NAME + UpaymentGatewayAppUtils.SLASH + UpaymentGatewayAppUtils.TPOST_MULTIPLE_REFUND_DELETE)

                }
                val conn = url!!.openConnection() as HttpURLConnection
                // Set the request method to POST
                conn.requestMethod = "POST"

                // Enable output and input streams
                conn.doOutput = true
                conn.doInput = true

                var auth_header_token= UpaymentGateway.sdk_api_key
                if(auth_header_token.isNullOrEmpty()){
                    auth_header_token = UpaymentGateway.upaymentGatewayAppPreferences.getString(
                        UpaymentGatewayAppUtils.KEY_HEADER_TOKEN,
                        ""
                    )

                }

                //oxxnDz0ES48qyaT96f8VG6YYyFr0krk2akJI7LH5
                conn.setRequestProperty("Authorization", "Bearer " + auth_header_token);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                conn.setRequestProperty("Accept", "application/json")
                conn.setRequestProperty("charset", "utf-8")

                // Write the POST data to the connection's output stream
                val outputStream: OutputStream = conn.outputStream
                val writer = BufferedWriter(OutputStreamWriter(outputStream, "UTF-8"))
                writer.write(postData.toString())
                writer.flush()
                writer.close()
                outputStream.close()
                UpaymentGatewayLog.d("-> Multi Refund Delete API Post Data !"+ postData.toString(),true)


                // Get the response from the server
                val responseCode = conn.responseCode
                UpaymentGatewayLog.d("-> Multi Refund Delete API Success !"+ responseCode.toString(),true)

                if (responseCode == 201) {
                    val inputStream = BufferedReader(InputStreamReader(conn.inputStream))
                    val responseStringBuilder = StringBuilder()
                    var line: String?
                    while (inputStream.readLine().also { line = it } != null) {
                        responseStringBuilder.append(line).append("\n")
                    }
                    inputStream.close()
                    response = responseStringBuilder.toString()
                    UpaymentGatewayLog.d("-> Multi Refund Delete API Success !"+ response.toString(),true)

                } else {
                    response = "Error"
                    UpaymentGatewayLog.d("-> Multi Refund Delete API Error !"+ response.toString(),true)

                }
                conn.disconnect()

                val jsonObject = JSONObject(response)
                var status =false
                if(jsonObject!=null && jsonObject.has("status")){
                    status  = jsonObject.getBoolean("status")
                }
                if (status) {
                      try {
                            // If the response is a JSON object
                            // Process the JSON data here as needed
                            val messageData = jsonObject.getString("message")
                            var isMultivendorRefund= 0
                            if(messageData!=null && !TextUtils.isEmpty(messageData)){

                            }
                            val jsonObjectData= jsonObject.getJSONObject("data")
                            if(jsonObjectData !=null){


                                val gson = Gson()
                                val responseMultivendorRefundData : ResponseMultiRefundDelete =     gson.fromJson(jsonObject.toString(), ResponseMultiRefundDelete::class.java)
                                val uPaymentCallBack = UpaymentGateway.UPaymentCallBack
                                uPaymentCallBack.sucessMultiRefundDelete(responseMultivendorRefundData)
                                // send call back from here to Activity
                                return  jsonObject.toString()

                            }

                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                } else {
                    UpaymentGatewayLog.e("postData SingleRefund Failed : " + "Error")
                    val uPaymentCallBack = UpaymentGateway.UPaymentCallBack
                    uPaymentCallBack.errorPayUpayment("Error")
                    return "error"
                }

            } catch (e: Exception) {
                response = "Error"
            }
            return response
        }

        fun postAddCards(postData: String, isWhitelabled: Boolean, properties: Customer):String {

            var response = ""
            try {

                val objData=  JSONObject()
                var urlTest:URL ?= null
                var url: URL? = null
                URLEncoder.encode(
                    UpaymentGatewayAppUtils.ABC + UpaymentGatewayAppUtils.NAME + UpaymentGatewayAppUtils.SLASH + UpaymentGatewayAppUtils.TPOST_ADD_CARDS

                )
                if (isWhitelabled)
                    url =
                        URL(UpaymentGatewayAppUtils.ABC + UpaymentGatewayAppUtils.NAME + UpaymentGatewayAppUtils.SLASH + UpaymentGatewayAppUtils.TPOST_ADD_CARDS)

                else {
                    url =
                        URL(UpaymentGatewayAppUtils.ABC + UpaymentGatewayAppUtils.NAME + UpaymentGatewayAppUtils.SLASH + UpaymentGatewayAppUtils.TPOST_ADD_CARDS)

                }
                val conn = url!!.openConnection() as HttpURLConnection
                // Set the request method to POST
                conn.requestMethod = "POST"

                // Enable output and input streams
                conn.doOutput = true
                conn.doInput = true

                var auth_header_token= UpaymentGateway.sdk_api_key
                if(auth_header_token.isNullOrEmpty()){
                    auth_header_token = UpaymentGateway.upaymentGatewayAppPreferences.getString(
                        UpaymentGatewayAppUtils.KEY_HEADER_TOKEN,
                        ""
                    )

                }

                //oxxnDz0ES48qyaT96f8VG6YYyFr0krk2akJI7LH5
                conn.setRequestProperty("Authorization", "Bearer " + auth_header_token);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                conn.setRequestProperty("Accept", "application/json")
                conn.setRequestProperty("charset", "utf-8")

                UpaymentGatewayLog.d("->  Add Cards API Post !"+ postData.toString(),true)


                // Write the POST data to the connection's output stream
                val outputStream: OutputStream = conn.outputStream
                val writer = BufferedWriter(OutputStreamWriter(outputStream, "UTF-8"))
                writer.write(postData.toString())
                writer.flush()
                writer.close()
                outputStream.close()

                // Get the response from the server
                val responseCode = conn.responseCode
                UpaymentGatewayLog.d("->  Add Cards API Success !"+ responseCode.toString(),true)

                if (responseCode == 201) {
                    val inputStream = BufferedReader(InputStreamReader(conn.inputStream))
                    val responseStringBuilder = StringBuilder()
                    var line: String?
                    while (inputStream.readLine().also { line = it } != null) {
                        responseStringBuilder.append(line).append("\n")
                    }
                    inputStream.close()
                    response = responseStringBuilder.toString()
                    UpaymentGatewayLog.d("-> Add Cards API Success !"+ response.toString(),true)

                } else {
                    response = "Error"
                    UpaymentGatewayLog.d("->  Add Cards API Error !"+ response.toString(),true)

                }
                conn.disconnect()

                val jsonObject = JSONObject(response)
                var status =false
                if(jsonObject.has("status")){
                    status  = jsonObject.getBoolean("status")
                }
                if (status) {
                    try {
                        // If the response is a JSON object
                        // Process the JSON data here as needed
                        val messageData = jsonObject.getString("message")
                        var isMultivendorRefund= 0
                        if(messageData!=null && !TextUtils.isEmpty(messageData)){

                        }
                        val jsonObjectData= jsonObject.getJSONObject("data")
                        if(jsonObjectData !=null){

                            // send call back from here to Activity

                            val gson = Gson()
                            val responseAddCard : ResponseAddCard =     gson.fromJson(jsonObject.toString(), ResponseAddCard::class.java)
                            val uPaymentCallBack = UpaymentGateway.UPaymentCallBack
                            uPaymentCallBack.sucessAddCard(responseAddCard)
                            return  jsonObject.toString()

                        }

                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                } else {
                    UpaymentGatewayLog.e("postData SingleRefund Failed : " + "Error")
                    val uPaymentCallBack = UpaymentGateway.UPaymentCallBack
                    uPaymentCallBack.errorPayUpayment("Error")
                    return "error"
                }

            } catch (e: Exception) {
                response = "Error"
            }
            return response
        }


        fun getcustomerUniqueToken(mobile: String, isWhitelabled: Boolean):String {

            var response = ""
            try {

                var urlTest:URL ?= null
                var url: URL? = null
                URLEncoder.encode(
                    UpaymentGatewayAppUtils.BASEURLLISTPAYMENT + UpaymentGatewayAppUtils.TGET_UNIQUE_TOKEN

                )
                if (isWhitelabled)
                    url =
                        URL(UpaymentGatewayAppUtils.BASEURLLISTPAYMENT + UpaymentGatewayAppUtils.TGET_UNIQUE_TOKEN)

                else {
                    url =
                        URL(UpaymentGatewayAppUtils.BASEURLLISTPAYMENT + UpaymentGatewayAppUtils.TGET_UNIQUE_TOKEN)

                }
                val conn = url!!.openConnection() as HttpURLConnection
                // Set the request method to POST
                conn.requestMethod = "POST"

                // Enable output and input streams
                conn.doOutput = true
                conn.doInput = true

                var auth_header_token= UpaymentGateway.sdk_api_key
                if(auth_header_token.isNullOrEmpty()){
                    auth_header_token = UpaymentGateway.upaymentGatewayAppPreferences.getString(
                        UpaymentGatewayAppUtils.KEY_HEADER_TOKEN,
                        ""
                    )

                }

                //oxxnDz0ES48qyaT96f8VG6YYyFr0krk2akJI7LH5
                conn.setRequestProperty("Authorization", "Bearer " + auth_header_token);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                conn.setRequestProperty("Accept", "application/json")
                conn.setRequestProperty("charset", "utf-8")

                //  conn.setRequestProperty("Content-Type", "application/json");


                // Write the POST data to the connection's output stream
                val outputStream: OutputStream = conn.outputStream
                val writer = BufferedWriter(OutputStreamWriter(outputStream, "UTF-8"))
                writer.write(mobile.toString())
                writer.flush()
                writer.close()
                outputStream.close()

                // Get the response from the server
                val responseCode = conn.responseCode
                UpaymentGatewayLog.d("-> Get Customer Unique Token API Status!"+ responseCode.toString(),true)

                if (responseCode == 201) {
                    val inputStream = BufferedReader(InputStreamReader(conn.inputStream))
                    val responseStringBuilder = StringBuilder()
                    var line: String?
                    while (inputStream.readLine().also { line = it } != null) {
                        responseStringBuilder.append(line).append("\n")
                    }
                    inputStream.close()
                    response = responseStringBuilder.toString()
                    UpaymentGatewayLog.d("-> Get Customer Unique Token Success Response!"+ response.toString(),true)

                } else {
                    response = "Error"
                    UpaymentGatewayLog.d("-> Get Customer Unique Token Error Response!"+ response.toString(),true)

                }
                conn.disconnect()

                val jsonObject = JSONObject(response)
                var status =false
                if(jsonObject.has("status")){
                    status  = jsonObject.getBoolean("status")
                }
                if (status) {
                    try {
                        try {
                            // If the response is a JSON object
                            // Process the JSON data here as needed
                            val messageData = jsonObject.getString("message")
                            if(messageData!=null && !TextUtils.isEmpty(messageData)){

                            }
                            val jsonObjectData= jsonObject.getJSONObject("data")
                            if(jsonObjectData !=null){
                                val customerUniqueToken =    jsonObjectData.getString("customerUniqueToken")
                                if(customerUniqueToken!=null){
                                    PreferenceHelper.customPreference(context = UpaymentGateway.getContext()  ,"customer_unique_token").customerToken=customerUniqueToken
                                    return customerUniqueToken
                                }
                            }
                        } catch (e: Exception) {
                            // Handle any parsing or processing errors here
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                } else {
                    UpaymentGatewayLog.e("postData failed : " + "Error")
                    val uPaymentCallBack = UpaymentGateway.UPaymentCallBack
                    uPaymentCallBack.errorPayUpayment("Error on Getting paymentURL")
                    return "error"
                }

            } catch (e: Exception) {
                response = "Error"
            }
            return response
        }

        fun getRetriveCard(mobile: String, isWhitelabled: Boolean):String {

            var response = ""
            try {

                var urlTest:URL ?= null
                var url: URL? = null
                URLEncoder.encode(
                    UpaymentGatewayAppUtils.ABC + UpaymentGatewayAppUtils.NAME + UpaymentGatewayAppUtils.SLASH + UpaymentGatewayAppUtils.TGET_RETRIVE_CARD

                )
                if (isWhitelabled)
                    url =
                        URL(UpaymentGatewayAppUtils.ABC + UpaymentGatewayAppUtils.NAME + UpaymentGatewayAppUtils.SLASH + UpaymentGatewayAppUtils.TGET_RETRIVE_CARD)

                else {
                    url =
                        URL(UpaymentGatewayAppUtils.ABC + UpaymentGatewayAppUtils.NAME + UpaymentGatewayAppUtils.SLASH + UpaymentGatewayAppUtils.TGET_RETRIVE_CARD)

                }
                val conn = url!!.openConnection() as HttpURLConnection
                // Set the request method to POST
                conn.requestMethod = "POST"

                // Enable output and input streams
                conn.doOutput = true
                conn.doInput = true

                var auth_header_token= UpaymentGateway.sdk_api_key
                if(auth_header_token.isNullOrEmpty()){
                    auth_header_token = UpaymentGateway.upaymentGatewayAppPreferences.getString(
                        UpaymentGatewayAppUtils.KEY_HEADER_TOKEN,
                        ""
                    )

                }

                UpaymentGatewayLog.d("->  Retrive card API Request!"+ mobile.toString(),true)


                //oxxnDz0ES48qyaT96f8VG6YYyFr0krk2akJI7LH5
                conn.setRequestProperty("Authorization", "Bearer " + auth_header_token);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                conn.setRequestProperty("Accept", "application/json")
                conn.setRequestProperty("charset", "utf-8")

                //  conn.setRequestProperty("Content-Type", "application/json");


                // Write the POST data to the connection's output stream
                val outputStream: OutputStream = conn.outputStream
                val writer = BufferedWriter(OutputStreamWriter(outputStream, "UTF-8"))
                writer.write(mobile.toString())
                writer.flush()
                writer.close()
                outputStream.close()

                // Get the response from the server
                val responseCode = conn.responseCode
                UpaymentGatewayLog.d("->  Retrive card API response code!"+ mobile.toString(),true)

                if (responseCode == 201) {
                    val inputStream = BufferedReader(InputStreamReader(conn.inputStream))
                    val responseStringBuilder = StringBuilder()
                    var line: String?
                    while (inputStream.readLine().also { line = it } != null) {
                        responseStringBuilder.append(line).append("\n")
                    }
                    inputStream.close()
                    response = responseStringBuilder.toString()
                    UpaymentGatewayLog.d("->  Retrive card API response !"+ response.toString(),true)

                } else {
                    response = "Error"
                    UpaymentGatewayLog.d("->  Retrive card API response error!"+ response.toString(),true)

                }
                conn.disconnect()

                val jsonObject = JSONObject(response)
                var status =false
                if(jsonObject!=null && jsonObject.has("status")){
                    status  = jsonObject.getBoolean("status")
                }
                if (status) {
                    try {
                        try {
                            // If the response is a JSON object
                            // Process the JSON data here as needed
                            val messageData = jsonObject.getString("message")
                            if(messageData!=null && !TextUtils.isEmpty(messageData)){

                            }
                            val jsonObjectData= jsonObject.getJSONObject("data")

                                if(jsonObjectData !=null){

                                    val gson = Gson()
                                    val responseMultivendorRefundData : ResponseRetriveCard=     gson.fromJson(jsonObject.toString(), ResponseRetriveCard::class.java)

                                    if(responseMultivendorRefundData!=null){
                                        val uPaymentCallBack = UpaymentGateway.UPaymentCallBack
                                        uPaymentCallBack.sucessRetriveCard(responseMultivendorRefundData)
                                    }


                                   return response
                                }

                        } catch (e: Exception) {
                            // Handle any parsing or processing errors here
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                } else {
                    UpaymentGatewayLog.e("postData failed : " + "Error")
                    val uPaymentCallBack = UpaymentGateway.UPaymentCallBack
                    uPaymentCallBack.errorPayUpayment("Error on Getting paymentURL")
                    return "error"
                }

            } catch (e: Exception) {
                response = "Error"
            }
            return response
        }


    }
}