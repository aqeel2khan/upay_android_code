package com.upayment.upaymentsdk.track;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import com.upayment.upaymentsdk.UPaymentCallBack;
import com.upayment.upaymentsdk.activity.PaymentSrcListSelection;
import com.upayment.upaymentsdk.track.card.Customer;
import com.upayment.upaymentsdk.track.charge.UpaymentGatewayEvent3;
import com.upayment.upaymentsdk.track.customertoken.PostCustomerToken;
import com.upayment.upaymentsdk.track.refund.PostDeleteSingleRefundData;
import com.upayment.upaymentsdk.track.refund.PostSingleRefundData;
import com.upayment.upaymentsdk.track.refund.delete.PostMultivendorRefundDelete;
import com.upayment.upaymentsdk.util.PreferenceHelper;
import com.upayment.upaymentsdk.util.UpaymentGatewayAppPreferences;
import com.upayment.upaymentsdk.util.UpaymentGatewayAppUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Adil on 25/11/2016.
 */
public class UpaymentGateway {

    private static final String TAG = UpaymentGateway.class.getSimpleName();
   protected   static final int HANDLER_MESSAGE_RETRY = 1;
    static String mAdoDomain=null;
    public static com.upayment.upaymentsdk.UPaymentCallBack UPaymentCallBack;
    private static UpaymentGateway mAdoInstance;
     public static Context mAppContext;
    protected Executor mAdoExecutor = Executors.newSingleThreadExecutor();
    static UpaymentGatewayAppPreferences upaymentGatewayAppPreferences;
    @Nullable

    static String sdk_merchant_id="";
    static String sdk_api_key="";
    static String sdk_sandbox_production_key="";


    protected Handler mUiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_MESSAGE_RETRY:
                    UpaymentGatewayLog.d("Retry strategy");
                    UpaymentGateway.getInstance().track(null, UPaymentCallBack);
                    break;
            }
        }
    };

    private UpaymentGateway() {
        //cannot be initialized anywhere else
    }

    public static UpaymentGateway getInstance() {
        if (mAdoInstance == null) {
            mAdoInstance = new UpaymentGateway();
        }
        return mAdoInstance;
    }

   // sdk_merchant_id = key  , api_key=  sdk_api_key1
    public static void init(Context context,String merchant_id,String api_key, boolean token,String sandboxProduction) {
        UpaymentGatewayLog.LOG_ENABLED = token;
        sdk_merchant_id=merchant_id;
        sdk_api_key=api_key;
        sdk_sandbox_production_key = sandboxProduction;


        UpaymentGatewayLog.e("Install process inside first init");

        UpaymentGatewayLog.assertCondition(mAppContext == null && mAdoDomain == null, "Init must be called only once.");
        UpaymentGatewayLog.assertCondition(UpaymentGatewayHelper.isPermissionGranted(context, Manifest.permission.INTERNET),
                "Init failed : permission is missing. You must add permission " +
                        Manifest.permission.INTERNET + " in your app Manifest.xml.");
        UpaymentGatewayLog.assertCondition(UpaymentGatewayHelper.isPermissionGranted(context, Manifest.permission.ACCESS_NETWORK_STATE),
                "Init failed : permission is missing: Your must add permission " + Manifest.permission
                        .ACCESS_NETWORK_STATE + " in your app Manifest.xml");
        UpaymentGatewayLog.assertCondition(UpaymentGatewayHelper.isPermissionGranted(context, Manifest.permission.ACCESS_WIFI_STATE),
                "Init failed : permission is missing: Your must add permission " + Manifest.permission
                        .ACCESS_WIFI_STATE + " in your app Manifest.xml");

        UpaymentGatewayLog.assertCondition(context != null, "Init failed : context is null. You must provide a valid context.");
//
        mAppContext = context;
        mAdoDomain = UpaymentGatewayAppUtils.ABC + UpaymentGatewayAppUtils.NAME+ UpaymentGatewayAppUtils.SLASH+ UpaymentGatewayAppUtils.TGET;
        upaymentGatewayAppPreferences = new UpaymentGatewayAppPreferences(context);
            init(sdk_api_key);

        upaymentGatewayAppPreferences.putString(UpaymentGatewayAppUtils.KEY_SANDBOX_VS_PRODUCTION,sandboxProduction);

    }
    private static void init(final String auth_header_token) {
        ArrayList<String> arrayList= new ArrayList<>();
        // Get from Share Pref
        sdk_api_key = auth_header_token;
           upaymentGatewayAppPreferences.putString(UpaymentGatewayAppUtils.KEY_HEADER_TOKEN,auth_header_token);

    }

    public static void initAuthHeaderToken(final String auth_header_token) {
        ArrayList<String> arrayList= new ArrayList<>();
        // Get from Share Pref
        sdk_api_key = auth_header_token;
        upaymentGatewayAppPreferences.putString(UpaymentGatewayAppUtils.KEY_HEADER_TOKEN,auth_header_token);

    }
   public static Context getContext() {
        UpaymentGatewayLog.assertCondition(mAppContext != null, "The SDK has not been initialized. You must call AdosizAnalytics" +
                ".init(Context, String) once.");
        return mAppContext;
    }
    public static void setContext(Context mAppContext){
        if(mAppContext==null){
            UpaymentGateway.mAppContext=mAppContext;
        }
    }
    public void track(final UpaymentGatewayEvent properties, UPaymentCallBack callBack) {
        this.UPaymentCallBack= callBack;
        List<String> storedProperties = null;
        try {
            if(properties!=null ){
                storedProperties = UpaymentGatewayFileHelper.getLines();
                if(storedProperties!=null && storedProperties.size()>0){
                    String line = storedProperties.get(0);
                    JSONObject lineJson = new JSONObject(line);
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(lineJson);
                    UpaymentGatewayFileHelper.deleteLines(jsonArray.length());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        UpaymentGatewayLog.assertCondition(mAdoDomain != null, "The SDK has not been initialized. You must call UpaymentGateway" +
                ".init(Context, String) once.");
        if (properties == null) {
            mAdoExecutor.execute(new AdosizAnalyticsStoredPropTracker(mUiHandler));
        } else {

            mAdoExecutor.execute(new UpaymentGatewayPropTracker(properties, mUiHandler));
        }
    }

    public void trackKotlin2(final UpaymentGatewayEvent3 properties, UPaymentCallBack callBack)  {
        UpaymentGatewayLog.d("track process 3");
        try {
            String convertJsonString = new Gson().toJson(properties);
            JSONObject jsonObject = new JSONObject(convertJsonString);

            this.UPaymentCallBack= callBack;
            List<String> storedProperties = null;
            try {
                if(properties!=null ){
                    storedProperties = UpaymentGatewayFileHelper.getLines();
                    if(storedProperties!=null && storedProperties.size()>0){
                        String line = storedProperties.get(0);
                        JSONObject lineJson = new JSONObject(line);
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(lineJson);
                        UpaymentGatewayFileHelper.deleteLines(jsonArray.length());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            UpaymentGatewayLog.assertCondition(mAdoDomain != null, "The SDK has not been initialized. You must call UpaymentGateway" +
                    ".init(Context, String) once.");
            if (properties == null) {
                mAdoExecutor.execute(new AdosizAnalyticsStoredPropTrackerKotlin(mUiHandler));
            } else {
                if(   properties.is_whitelabled()){
                    Intent intentSend=    new Intent(UpaymentGateway.mAppContext, PaymentSrcListSelection.class);
              //      JSONObject   jsonData=   properties.getJson();
                    if(jsonObject!=null){

                    }
                    intentSend.putExtra("eventData", properties);
                    intentSend.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentSend.putExtra("postDat","UrlToLoad");
                    intentSend.putExtra("postDat","UrlToLoad");
                    intentSend.putExtra("successUrl","key_s_url");
                    intentSend.putExtra("errorUrl","key_e_url");
                    intentSend.putExtra("notifyUrl","key_noti_url");
                    intentSend.putExtra("callback","key_noti_url");
                    UpaymentGateway.mAppContext.startActivity(intentSend);
                    return;
                }else if(!properties.is_whitelabled()){
                    mAdoExecutor.execute(new UpaymentGatewayPropTrackerKotlin2(properties, mUiHandler));

                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void trackChargeFromPaymentList(final UpaymentGatewayEvent3 properties, UPaymentCallBack callBack)  {
        UpaymentGatewayLog.d("track process 3");
        try {
            String convertJsonString = new Gson().toJson(properties);
            JSONObject jsonObject = new JSONObject(convertJsonString);

//            this.UPaymentCallBack= callBack;
            List<String> storedProperties = null;
            try {
                if(properties!=null ){
                    storedProperties = UpaymentGatewayFileHelper.getLines();
                    if(storedProperties!=null && storedProperties.size()>0){
                        String line = storedProperties.get(0);
                        JSONObject lineJson = new JSONObject(line);
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(lineJson);
                        UpaymentGatewayFileHelper.deleteLines(jsonArray.length());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            UpaymentGatewayLog.assertCondition(mAdoDomain != null, "The SDK has not been initialized. You must call UpaymentGateway" +
                    ".init(Context, String) once.");
            if (properties == null) {
                mAdoExecutor.execute(new AdosizAnalyticsStoredPropTrackerKotlin(mUiHandler));
            } else {
                if(   properties.is_whitelabled()){
                    Intent intentSend=    new Intent(UpaymentGateway.mAppContext, PaymentSrcListSelection.class);
                    //      JSONObject   jsonData=   properties.getJson();
                    if(jsonObject!=null){

                    }
                    intentSend.putExtra("eventData", properties);
                    intentSend.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentSend.putExtra("postDat","UrlToLoad");
                    intentSend.putExtra("postDat","UrlToLoad");
                    intentSend.putExtra("successUrl","key_s_url");
                    intentSend.putExtra("errorUrl","key_e_url");
                    intentSend.putExtra("notifyUrl","key_noti_url");
                    intentSend.putExtra("callback","key_noti_url");
                    UpaymentGateway.mAppContext.startActivity(intentSend);
                    return;
                }else if(!properties.is_whitelabled()){
                    mAdoExecutor.execute(new UpaymentGatewayPropTrackerKotlin2(properties, mUiHandler));

                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    public void trackSingleRefund(final PostSingleRefundData properties, UPaymentCallBack callBack,Boolean is_whitelabled )  {
        UpaymentGatewayLog.d("track process 3");
        try {
            String convertJsonString = new Gson().toJson(properties);
            if(convertJsonString!=null){

            }

            this.UPaymentCallBack= callBack;
            List<String> storedProperties = null;
            try {
                if(properties!=null ){
                    storedProperties = UpaymentGatewayFileHelper.getLines();
                    if(storedProperties!=null && storedProperties.size()>0){
                        String line = storedProperties.get(0);
                        JSONObject lineJson = new JSONObject(line);
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(lineJson);
                        UpaymentGatewayFileHelper.deleteLines(jsonArray.length());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            UpaymentGatewayLog.assertCondition(mAdoDomain != null, "The SDK has not been initialized. You must call UpaymentGateway" +
                    ".init(Context, String) once.");
            if (properties == null) {
                mAdoExecutor.execute(new AdosizAnalyticsStoredPropTrackerKotlin(mUiHandler));
            } else {

                mAdoExecutor.execute(new UpaymentGatewayRefundTrack(properties, mUiHandler,is_whitelabled));

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void trackDeleteSingleRefund(final PostDeleteSingleRefundData properties, UPaymentCallBack callBack,Boolean is_whitelabled )  {
        UpaymentGatewayLog.d("track process 3");
        try {
            String convertJsonString = new Gson().toJson(properties);

            this.UPaymentCallBack= callBack;
            List<String> storedProperties = null;
            try {
                if(properties!=null ){
                    storedProperties = UpaymentGatewayFileHelper.getLines();
                    if(storedProperties!=null && storedProperties.size()>0){
                        String line = storedProperties.get(0);
                        JSONObject lineJson = new JSONObject(line);
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(lineJson);
                        UpaymentGatewayFileHelper.deleteLines(jsonArray.length());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            UpaymentGatewayLog.assertCondition(mAdoDomain != null, "The SDK has not been initialized. You must call UpaymentGateway" +
                    ".init(Context, String) once.");
            if (properties == null) {

                mAdoExecutor.execute(new AdosizAnalyticsStoredPropTrackerKotlin(mUiHandler));

            } else {

                mAdoExecutor.execute(new UpaymentGatewayDeleteRefundTrack(properties, mUiHandler,is_whitelabled));

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void trackDeleteMultiRefund(final PostMultivendorRefundDelete properties, UPaymentCallBack callBack,Boolean is_whitelabled )  {
        UpaymentGatewayLog.d("track process 3");
        try {
            String convertJsonString = new Gson().toJson(properties);

            this.UPaymentCallBack= callBack;
            List<String> storedProperties = null;
            try {
                if(properties!=null ){
                    storedProperties = UpaymentGatewayFileHelper.getLines();
                    if(storedProperties!=null && storedProperties.size()>0){
                        String line = storedProperties.get(0);
                        JSONObject lineJson = new JSONObject(line);
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(lineJson);
                        UpaymentGatewayFileHelper.deleteLines(jsonArray.length());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            UpaymentGatewayLog.assertCondition(mAdoDomain != null, "The SDK has not been initialized. You must call UpaymentGateway" +
                    ".init(Context, String) once.");
            if (properties == null) {

                mAdoExecutor.execute(new AdosizAnalyticsStoredPropTrackerKotlin(mUiHandler));

            } else {

                mAdoExecutor.execute(new UpaymentGatewayMultiDeleteRefundTrack(properties, mUiHandler,is_whitelabled));

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void trackAddCards(final Customer customer , UPaymentCallBack callBack,Boolean is_whitelabled )  {
        UpaymentGatewayLog.d("track process 3");
        try {

            this.UPaymentCallBack= callBack;
            List<String> storedProperties = null;
            try {
                if(customer!=null ){
                    storedProperties = UpaymentGatewayFileHelper.getLines();
                    if(storedProperties!=null && storedProperties.size()>0){
                        String line = storedProperties.get(0);
                        JSONObject lineJson = new JSONObject(line);
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(lineJson);
                        UpaymentGatewayFileHelper.deleteLines(jsonArray.length());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            UpaymentGatewayLog.assertCondition(mAdoDomain != null, "The SDK has not been initialized. You must call UpaymentGateway" +
                    ".init(Context, String) once.");
            if (customer == null) {

                mAdoExecutor.execute(new AdosizAnalyticsStoredPropTrackerKotlin(mUiHandler));

            } else {

                mAdoExecutor.execute(new UpaymentGatewayAddCardsTrack(customer, mUiHandler,is_whitelabled));

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void getCustomerUniqueToken( UPaymentCallBack callBack, final boolean is_whitelabel)  {
        UpaymentGatewayLog.d("track process 3");
        try {

            this.UPaymentCallBack= callBack;

            UpaymentGatewayLog.assertCondition(mAdoDomain != null, "The SDK has not been initialized. You must call UpaymentGateway" +
                    ".init(Context, String) once.");
            String customerToken= String.valueOf(
                    PreferenceHelper.customPreference( UpaymentGateway.getContext()  ,"customer_unique_token"));
            PostCustomerToken dataPost  =new  PostCustomerToken();
            Long numberRandom=   UpaymentGatewayConfig.generateNumber();
            dataPost.setCustomerUniqueToken(numberRandom.toString());

            String   dataSend = new Gson().toJson(dataPost);

            if(customerToken==null || customerToken.isEmpty()){

                mAdoExecutor.execute(new UpaymentGatewayCustomerUniqueToken(dataSend, mUiHandler,is_whitelabel));

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void getRetriveCard( UPaymentCallBack callBack, final boolean is_whitelabel)  {
        UpaymentGatewayLog.d("track process 3");
        try {

            this.UPaymentCallBack= callBack;

            UpaymentGatewayLog.assertCondition(mAdoDomain != null, "The SDK has not been initialized. You must call UpaymentGateway" +
                    ".init(Context, String) once.");
            String customerToken= String.valueOf(
                    PreferenceHelper.customPreference( UpaymentGateway.getContext()  ,"customer_unique_token"));
            PostCustomerToken dataPost  =new  PostCustomerToken();
            Long numberRandom=   UpaymentGatewayConfig.generateNumber();
            dataPost.setCustomerUniqueToken(numberRandom.toString());

            String   dataSend = new Gson().toJson(dataPost);


                mAdoExecutor.execute(new UpaymentGatewayRetriveCard(dataSend, mUiHandler,is_whitelabel));


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}