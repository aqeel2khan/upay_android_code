package com.upayment.upaymentsdk.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.upayment.upaymentsdk.R
import com.upayment.upaymentsdk.UPaymentCallBack
import com.upayment.upaymentsdk.track.CreateInvoiceResponse
import com.upayment.upaymentsdk.track.HttpPostRequest
import com.upayment.upaymentsdk.track.UpaymentGateway
import com.upayment.upaymentsdk.track.card.addcard.ResponseAddCard
import com.upayment.upaymentsdk.track.card.retrive.ResponseRetriveCard
import com.upayment.upaymentsdk.track.charge.UpaymentGatewayEvent3
import com.upayment.upaymentsdk.track.refund.SingleDeleteRefundResponse
import com.upayment.upaymentsdk.track.refund.SingleRefundResponse
import com.upayment.upaymentsdk.track.refund.multidelete.ResponseMultiRefundDelete
import com.upayment.upaymentsdk.track.refund.multirefund.ResponseMultivendorRefund
import com.upayment.upaymentsdk.util.PreferenceHelper
import com.upayment.upaymentsdk.util.PreferenceHelper.customerToken
import org.json.JSONObject
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PaymentSrcListSelection:AppCompatActivity(), UPaymentCallBack {
    lateinit var radioGroup: RadioGroup
    val mExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    lateinit var radioButton1 :RadioButton
    lateinit var radioButton2 :RadioButton

    lateinit var radioButton3 :RadioButton

    lateinit var radioButton4 :RadioButton
    lateinit var radioButton5 :RadioButton

    lateinit var radioButton6 :RadioButton



    private lateinit var uPaymentCallBack: UPaymentCallBack
    var executor: Executor = Executors.newSingleThreadExecutor()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment_source_list_selection_activity);
        uPaymentCallBack = UpaymentGateway.UPaymentCallBack
        radioGroup = findViewById(R.id.radioGroup)

         radioButton1 = findViewById(R.id.radioOption1)
         radioButton2 = findViewById(R.id.radioOption2)

         radioButton3 = findViewById(R.id.radioOption3)

         radioButton4 = findViewById(R.id.radioOption4)

         radioButton5 = findViewById(R.id.radioOption5)

         radioButton6 = findViewById(R.id.radioOption6)

        val eventData: UpaymentGatewayEvent3? ;
        val userData = if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            eventData=    intent!!.getParcelableExtra("eventData", UpaymentGatewayEvent3::class.java)!!
        } else {
            eventData=    intent!!.getParcelableExtra<UpaymentGatewayEvent3>("eventData")!!
        }
        val     propertiesToString:String = Gson().toJson(eventData)

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            // TODO Auto-generated method stub
            if (radioButton1.isChecked()) {
                val sourcePayment = radioButton1.text.toString()
                eventData.paymentGateway.src = sourcePayment

                val propertiesToString: String = Gson().toJson(eventData)

                callOtherAPI(sourcePayment, eventData, propertiesToString)
                // do something
            } else if (radioButton2.isChecked()) {
                val sourcePayment = radioButton2.text.toString()
                eventData.paymentGateway.src = sourcePayment

                val propertiesToString: String = Gson().toJson(eventData)

                callOtherAPI(sourcePayment, eventData, propertiesToString)
                // do something
            } else if (radioButton3.isChecked()) {
                val sourcePayment = radioButton3.text.toString()
                eventData.paymentGateway.src = sourcePayment

                val propertiesToString: String = Gson().toJson(eventData)

                callOtherAPI(sourcePayment, eventData, propertiesToString)
                // do something
            } else if (radioButton4.isChecked()) {
                val sourcePayment = radioButton4.text.toString()
                eventData.paymentGateway.src = sourcePayment

                val propertiesToString: String = Gson().toJson(eventData)

                callOtherAPI(sourcePayment, eventData, propertiesToString)
                // do something
            } else if (radioButton5.isChecked()) {
                val sourcePayment = radioButton5.text.toString()
                eventData.paymentGateway.src = sourcePayment

                val propertiesToString: String = Gson().toJson(eventData)

                callOtherAPI(sourcePayment, eventData, propertiesToString)
                // do something
            }
        }

//        radioGroup.setOnCheckedChangeListener { group, checkedId ->
//            val radioButton = findViewById<RadioButton>(checkedId)
//            if (radioButton != null) {
//              val  sourcePayment = radioButton.text.toString()
//
//
//
//            }
//        }


        if(eventData!=null){
            mExecutor.submit {

                val response = HttpPostRequest.sendPostRequestList("sdk", propertiesToString, eventData)
                // Handle the API response here (e.g., updating UI on the main thread)
                runOnUiThread {
                    try {
                        val jsonObject = JSONObject(response)
                        val status:Boolean = jsonObject.getBoolean("status")
                        if(status){
                            radioGroup.visibility= View.VISIBLE
                            val messageData = jsonObject.getString("message")
                            if(messageData!=null && !TextUtils.isEmpty(messageData)){

                            }
                            val jsonObjectData= jsonObject.getJSONObject("data")
                            if(jsonObjectData !=null){
                                val jsonData =    jsonObjectData.getJSONObject("payButtons")
                                if(jsonData!=null){
                                    val knetStatus =   jsonData.getBoolean("knet")
                                    if(knetStatus){
                                        radioButton1.visibility = View.VISIBLE
                                        //   callOtherAPI("knet",eventData,propertiesToString)
                                    }
                                    val credit_card=     jsonData.getBoolean("cc")
                                    if(credit_card){
                                        radioButton2.visibility = View.VISIBLE
                                        //   callOtherAPI("cc", eventData, propertiesToString)
                                    }
                                    val  samsung_pay   = jsonData.getBoolean("samsung-pay")
                                    if(samsung_pay){
                                        radioButton3.visibility = View.VISIBLE
                                        // callOtherAPI("samsung-pay", eventData, propertiesToString)
                                    }
                                    val amex=   jsonData.getBoolean("amex")
                                    if(amex){
                                        radioButton4.visibility = View.VISIBLE

                                        // callOtherAPI("amex", eventData, propertiesToString)
                                    }
                                    val googlepay=   jsonData.getBoolean("google-pay")
                                    if(googlepay){
                                        radioButton5.visibility = View.VISIBLE
                                        // callOtherAPI("google-pay", eventData, propertiesToString)
                                    }
                                    val applePay=   jsonData.getBoolean("apple-pay")
                                    if(applePay){
                                        radioButton5.visibility = View.GONE
//                                    callOtherAPI("apple-pay", eventData, propertiesToString)
                                    }
                                }
                            }
                        }

                    } catch (e: Exception) {
                        e.toString()
                        // Handle any parsing or processing errors here
                    }
                }
            }
        }
//        uPaymentCallBack.errorPayUpayment("error")
    }

    fun callOtherAPI(keySrc: String, eventData: UpaymentGatewayEvent3, propertiesToString: String) {
        executor.execute(){
           // PreferenceHelper.customPreference(context = UpaymentGateway.getContext()  ,"customer_unique_token").customerToken=""

            eventData.paymentGateway.src=keySrc
            eventData.is_whitelabled= false
            UpaymentGateway.getInstance().trackChargeFromPaymentList(eventData,this)

            finish()

//            trackKotlin2(eventCharge,this)
//            val response = HttpPostRequest.sendPostRequest("url", propertiesToString.toString(), eventData)
//            if(response!=null){
//            }
        }
    }

    override fun callBackUpayment(postUpayData: PostUpayData?) {
        if(postUpayData!=null){

            if( UpaymentGateway.   UPaymentCallBack!=null){
                UpaymentGateway.   UPaymentCallBack.callBackUpayment(postUpayData)
                finish()

            }
        }
    }

    override fun errorPayUpayment(data: String?) {
    }

    override fun sucessCreateInvoice(invoiceResponse: CreateInvoiceResponse?) {
    }

    override fun sucessMultiRefundDelete(invoiceResponse: ResponseMultiRefundDelete?) {
    }

    override fun sucessAddCard(invoiceResponse: ResponseAddCard?) {
    }

    override fun sucessSingleRefund(invoiceResponse: SingleRefundResponse?) {
    }

    override fun sucessSingleDeleteRefund(invoiceResponse: SingleDeleteRefundResponse?) {
    }

    override fun sucessMultiRefund(invoiceResponse: ResponseMultivendorRefund?) {
    }

    override fun sucessRetriveCard(invoiceResponse: ResponseRetriveCard?) {
    }

    override fun failureMultiRefund(invoiceResponse: ResponseMultivendorRefund?) {
    }
}