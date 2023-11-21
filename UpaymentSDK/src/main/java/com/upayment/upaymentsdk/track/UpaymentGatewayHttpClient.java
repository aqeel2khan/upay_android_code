package com.upayment.upaymentsdk.track;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;


import android.webkit.CookieManager;
import com.upayment.upaymentsdk.UPaymentCallBack;
import com.upayment.upaymentsdk.activity.ActivityWeb;
import com.upayment.upaymentsdk.util.UpaymentGatewayAppPreferences;
import com.upayment.upaymentsdk.util.UpaymentGatewayAppUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


/**
 * Created by Adil on 15/03/2015.
 */
class UpaymentGatewayHttpClient {
    public static final int DEFAULT_CONNECTION_TIMEOUT = 5 * 1000;  // 5s
    private static volatile int mTimeOut = DEFAULT_CONNECTION_TIMEOUT;
    private static String key_s_url= "";
    private static String key_e_url="";
    private static String key_noti_url= "";
    private static String key_dev_mode="";
    private static String UrlToLoad="";
    public synchronized static boolean postData(String value, boolean trueCallBack) {
        String path=null;
        JSONObject jsonObjectSend=null;

        String jsonString=null;
        UpaymentGatewayLog.d("-> posting data : " + value);

        if(TextUtils.isEmpty(value)) {
            UPaymentCallBack uPaymentCallBack2=   (UpaymentGateway.UPaymentCallBack);
            uPaymentCallBack2.errorPayUpayment("No any Data");
            UpaymentGatewayLog.d("-> posting empty data : " + jsonString);
            return false;

        }
        try {

            String merId="";String userName="";String mPass="";String mAPI="";
            String mOrderId="";String mTotalPrice= "";String mCurrencyCode="";String mSuccUrl="";String mErrorUrl="";
            String mNotifyUrl=""; String gateWay="";
            if(null!=value){
                if(value!=null && value.length()>0){

                }
            }
            String urlTest= "";
            URL url=null;
                URLEncoder.encode(UpaymentGatewayAppUtils.ABC + UpaymentGatewayAppUtils.NAME+ UpaymentGatewayAppUtils.SLASH+ UpaymentGatewayAppUtils.TGET,"UTF-8");
                 urlTest = UpaymentGatewayAppUtils.ABC + UpaymentGatewayAppUtils.NAME+ UpaymentGatewayAppUtils.SLASH+ UpaymentGatewayAppUtils.TGET;

            UpaymentGatewayLog.d("->url final : " + urlTest);

                 url = new URL(UpaymentGatewayAppUtils.ABC + UpaymentGatewayAppUtils.NAME+ UpaymentGatewayAppUtils.SLASH+ UpaymentGatewayAppUtils.TGET);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(mTimeOut);
            conn.setReadTimeout(mTimeOut);
            conn.setRequestMethod("POST");

            // IF there is json data we want to do a post
            if (value != null) {
                // POST
                conn.setDoOutput(true); // Forces post
              //  conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + "oxxnDz0ES48qyaT96f8VG6YYyFr0krk2akJI7LH5");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("charset", "utf-8");

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write(value.toString());
                writer.flush();
                writer.close();
                //
            } else {
              //   GET
                conn.setDoOutput(false); // Defaults to false, but for readability
            }
            int statusCode = conn.getResponseCode();
            UpaymentGatewayLog.d("-> response code: " + statusCode);
            if (conn.getResponseCode() == 201) {
                CharSequence response = UpaymentGatewayHelper.toString(conn.getInputStream());
                if(response!=null){
                    response.toString();
                    JSONObject jsonObjectReponse= new JSONObject(response.toString());
                    if(jsonObjectReponse!=null){
                 Boolean status=       jsonObjectReponse.getBoolean("status");

                 if(!(status)  ){

                  String errorMessage=   jsonObjectReponse.getString("error_msg");
                  String error_code= jsonObjectReponse.getString("error_code");
                     UpaymentGatewayLog.e("postData failed : " + errorMessage);
                     UPaymentCallBack uPaymentCallBack=   (UpaymentGateway.UPaymentCallBack);
                     uPaymentCallBack.errorPayUpayment(error_code.toString());
                     return  true;
                 }else{
                     if(status){
                         if(jsonObjectReponse.has("data")){
                        JSONObject data=     jsonObjectReponse.getJSONObject("data");

                             UrlToLoad=    data.getString("link");
                         }

                         if(!TextUtils.isEmpty(UrlToLoad)){
                             try {
                                 new Handler(Looper.getMainLooper()).post(new Runnable() {
                                     @Override
                                     public void run() {
                                         Intent intentSend=    new Intent(UpaymentGateway.mAppContext, ActivityWeb.class);
                                         intentSend.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                         intentSend.putExtra("postDat",UrlToLoad);
                                         intentSend.putExtra("successUrl",key_s_url);
                                         intentSend.putExtra("errorUrl",key_e_url);
                                         intentSend.putExtra("notifyUrl",key_noti_url);
                                         UpaymentGateway.mAppContext.startActivity(intentSend);

                                     }
                                 });
                             } catch (Exception e) {
                                 e.printStackTrace();
                             }
                         }else{
                             UpaymentGatewayLog.e("postData failed : " + "Error");
                             UPaymentCallBack uPaymentCallBack=   (UpaymentGateway.UPaymentCallBack);
                             uPaymentCallBack.errorPayUpayment("Error on Getting paymentURL");
                             return  false;

                         }

                     }
                     return true;
                 }

                    }
                   // refereshData(jsonObjectSend);

                }
              //  refereshData(jsonObjectSend);
                return true;
            } else {
                return false;
            }
        } catch (IOException | JSONException e) {
            UpaymentGatewayLog.e("postData failed : " + e);
            if(trueCallBack){
                UPaymentCallBack uPaymentCallBack=   (UpaymentGateway.UPaymentCallBack);
                uPaymentCallBack.errorPayUpayment(e.toString());
            }

        }
        return false;
    }


    private static void refereshData(JSONObject jsonObjectSend) {
        if(null!=jsonObjectSend){
            try {
                String event_type=    jsonObjectSend.getString("event_type");
                if(event_type!=null && !TextUtils.isEmpty(event_type)){
                    refereshValue(event_type);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static void refereshValue(String event_type) {
            ArrayList<String> arrayList= new ArrayList<>();
        Context context= UpaymentGateway.getContext();
        if(null!=context){
            UpaymentGatewayAppPreferences upaymentGatewayAppPreferences = new UpaymentGatewayAppPreferences(UpaymentGateway.getContext());
            // Get from Share Pref
            if(null!= upaymentGatewayAppPreferences){
                String ValueSaved= upaymentGatewayAppPreferences.getString(UpaymentGatewayAppUtils.KEY_SAVE_EVENT,"");
                // Convert to List
                if(!TextUtils.isEmpty(ValueSaved))
                    arrayList=  UpaymentGatewayHelper.convertToArray(ValueSaved);
                // Store Event
                arrayList.add(event_type);
                String toSave=  UpaymentGatewayHelper.convertToString(arrayList);
                upaymentGatewayAppPreferences.putString(UpaymentGatewayAppUtils.KEY_SAVE_EVENT,toSave);
                if(UpaymentGatewayAppUtils.INSTALL.equalsIgnoreCase(event_type) ||  UpaymentGatewayAppUtils.ORGANIC.equalsIgnoreCase(event_type)){
                    UpaymentGatewayAppUtils.EVENT_INSTALL=true;
                }
            }
        }else{
       if(UpaymentGatewayAppUtils.INSTALL.equalsIgnoreCase(event_type) ||  UpaymentGatewayAppUtils.ORGANIC.equalsIgnoreCase(event_type)){
                UpaymentGatewayAppUtils.EVENT_INSTALL=true;
            }
            Log.d("Context","is null");
        }
    }



    public static boolean checkExit(ArrayList<String> list ,String event){

        if(list!=null && list.size()>0 && !TextUtils.isEmpty(event)){
            for(int i= 0 ; i <list.size(); i++){

                if(list.get(i).equalsIgnoreCase(event))
                    return true ;

            }
        }


        return false;
    }



}
