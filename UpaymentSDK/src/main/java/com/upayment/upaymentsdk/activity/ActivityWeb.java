package com.upayment.upaymentsdk.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.upayment.upaymentsdk.R;
import com.upayment.upaymentsdk.UPaymentCallBack;
import com.upayment.upaymentsdk.track.UpaymentGateway;
import com.upayment.upaymentsdk.track.UpaymentGatewayLog;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityWeb  extends AppCompatActivity {

    private static final int REQUEST_CODE_PAYMENT = 5;

    private boolean shouldOverride;

//    private WebChromeClient someActivityResultLauncher;
    private  ActivityResultLauncher<Intent> someActivityResultLauncher= null;


    private boolean status;

    private UPaymentCallBack uPaymentCallBack= null;
   private  String key_s_url=null;
   private String key_e_url=null;
   private String key_noti_url=null;
   private   WebView webView =null;
   private Boolean successPost= false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activty);

        uPaymentCallBack= (UpaymentGateway.UPaymentCallBack);

        webView = (WebView) findViewById(R.id.webView);

         someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Log.d("HandleSuccess","Success");

                            // There are no request codes
                            if(result!=null){
                                Intent data = result.getData();
                                String transactionId="";
                                String ref ="";
                                String refund_order_id="";
                                if(data!=null){

                                    if(data.hasExtra("tran_id")){
                                        transactionId = data.getStringExtra("tran_id");

                                    }
                                    if(data.hasExtra("ref")){
                                        ref = data.getStringExtra("ref");

                                    }
                                    if(data.hasExtra("refund_order_id")){
                                        refund_order_id = data.getStringExtra("refund_order_id");
                                    }


                                    Log.d("HandleSuccess","Success"+data.toString());
                                    Toast.makeText(ActivityWeb.this, "Success"+"TranId"+transactionId.toString()+"refundId"+refund_order_id, Toast.LENGTH_SHORT).show();

                                }else{
                                    Toast.makeText(ActivityWeb.this, "Success"+data.toString(), Toast.LENGTH_SHORT).show();

                                }
                            }else{
                                Toast.makeText(ActivityWeb.this, "Success"+ result.toString(), Toast.LENGTH_SHORT).show();

                            }

                            //  doSomeOperations();
                        }
                    }
                });

        Intent intent= getIntent();
        String urlLoad=  intent.getStringExtra("postData");

        key_s_url=  intent.getStringExtra("successUrl");
        key_e_url=  intent.getStringExtra("errorUrl");
        key_noti_url=  intent.getStringExtra("notifyUrl");

        if(TextUtils.isEmpty(key_s_url)){
            key_s_url= null;
        }
        if(TextUtils.isEmpty(key_e_url)){
            key_e_url= null;
        }
        if(TextUtils.isEmpty(key_noti_url)){
            key_noti_url= null;
        }


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

       // String urlTemp="https://apiv2api.upayments.com/get-pay-by-samsung?order_id=p8PS2GtfrGodZxD74RNzby9cDfsEP399&track_id=2tab587HaYmxfEf5kJRo0HWNSvOMuON3&transaction_id=2328669&ref_id=15269530&currency=KWD&amount=40.000&success_url=https://upayments.com/en/&failure_url=https://developers.upayments.com/";
// https://apiv2api.upayments.com/get-pay-by-samsung?order_id=o4SH0e022077v41371Vc10O2751Of824&track_id=0252f91u09182192r2050M19260323552223N553&transaction_id=2670134&ref_id=16590634&currency=KWD&amount=0.010&success_url=https://upayments.com/en/&failure_url=https://error.com

      // urlLoad = urlTemp;

    //   showSamsung1(urlLoad);
        MyWebViewClient myWebViewClient = new MyWebViewClient(someActivityResultLauncher);
        webView.setWebViewClient(myWebViewClient);

//        webView.setWebViewClient(new WebViewClient() {
//
//        @TargetApi(Build.VERSION_CODES.N)
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//
////            if(request.getUrl().toString().contains("http://dev-apiv2api.upayments.com/get-pay-by-samsung")){
////
////                showSamsung(request.getUrl().toString());
////                return true;
////            }
//            shouldOverride = false;
//
//            parseData( request.getUrl().toString());
//            return shouldOverride;
//        }
//        @SuppressWarnings("deprecation")
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//
////            if(url.toString().contains("http://dev-apiv2api.upayments.com/get-pay-by-samsung")){
////
////                showSamsung(url.toString());
////                return true;
////            }
//            shouldOverride = false;
//
//            parseData(url);
//            return shouldOverride;
//        }
//
//        @SuppressWarnings("deprecation")
//        @Override
//        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//            Log.i("URL", url);
////            if(url.toString().contains("http://dev-apiv2api.upayments.com/get-pay-by-samsung")){
////
////                showSamsung(url.toString());
////            }
//
//            parseData(url);
//
//            return super.shouldInterceptRequest(view, url);
//        }
//
//        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request)
//        {
//            http://dev-apiv2api.upayments.com/get-pay-by-samsung?order_id=QAB404KjMYoQvY6SHfH4y5Uir0KdcKs2&track_id=A0ynhEMEwrbgw839ypfnJI14JkUyk9D1&transaction_id=706564&ref_id=5955810&currency=KWD&amount=1.000&success_url=https://upayments.com/en/&failure_url=https://developers.upayments.com/
////            if(request.getUrl().toString().contains("http://dev-apiv2api.upayments.com/get-pay-by-samsung")){
////
////                showSamsung(request.getUrl().toString());
////            }
//            parseData(request.getUrl().toString());
//            return shouldInterceptRequest(view, request.getUrl().toString());
//        }
//
//        public void onLoadResource(WebView view, String url) {
//
//        }
//
//        public void onPageFinished(WebView view, String url) {
//            //  finish();
//            if(url!=null){
//
//                try {
//                    if(url.contains("http://dev-apiv2api.upayments.com/get-pay-by-samsung")) {
//
//                        showSamsung1(url);
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }
//
//        @Override
//        @TargetApi(Build.VERSION_CODES.M)
//        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//            super.onReceivedError(view, request, error);
//            final Uri uri = request.getUrl();
//          //  showSamsung(uri.toString());
//            //   handleError(view, error.getErrorCode(), error.getDescription().toString(), uri);
//        }
//
//    });
        webView.setWebChromeClient(new WebChromeClient());


        webView.loadUrl(urlLoad);





    }


    public class MyWebViewClient extends WebViewClient {

        private final ActivityResultLauncher<Intent> someActivityResultLauncher;

        public MyWebViewClient(ActivityResultLauncher<Intent> someActivityResultLauncher) {
            this.someActivityResultLauncher = someActivityResultLauncher;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

//            if(request.getUrl().toString().contains("http://dev-apiv2api.upayments.com/get-pay-by-samsung")){
//
//                showSamsung(request.getUrl().toString());
//                return true;
//            }
            shouldOverride = false;

            parseData( request.getUrl().toString());
            return shouldOverride;
        }
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

//            if(url.toString().contains("http://dev-apiv2api.upayments.com/get-pay-by-samsung")){
//
//                showSamsung(url.toString());
//                return true;
//            }
            shouldOverride = false;

            parseData(url);
            return shouldOverride;
        }

        @SuppressWarnings("deprecation")
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            Log.i("URL", url);
//            if(url.toString().contains("http://dev-apiv2api.upayments.com/get-pay-by-samsung")){
//
//                showSamsung(url.toString());
//            }

            parseData(url);

            return super.shouldInterceptRequest(view, url);
        }

        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request)
        {
            http://dev-apiv2api.upayments.com/get-pay-by-samsung?order_id=QAB404KjMYoQvY6SHfH4y5Uir0KdcKs2&track_id=A0ynhEMEwrbgw839ypfnJI14JkUyk9D1&transaction_id=706564&ref_id=5955810&currency=KWD&amount=1.000&success_url=https://upayments.com/en/&failure_url=https://developers.upayments.com/
//            if(request.getUrl().toString().contains("http://dev-apiv2api.upayments.com/get-pay-by-samsung")){
//
//                showSamsung(request.getUrl().toString());
//            }
            parseData(request.getUrl().toString());
            return shouldInterceptRequest(view, request.getUrl().toString());
        }

        public void onLoadResource(WebView view, String url) {

        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            //  finish();
            if(url!=null){

                try {
                    //https://apiv2api.upayments.com/get-pay-by-samsung?order_id=o4SH0e022077v41371Vc10O2751Of824&track_id=0252f91u09182192r2050M19260323552223N553&transaction_id=2670134&ref_id=16590634
                    // &currency=KWD&amount=0.010&success_url=https://upayments.com/en/&failure_url=https://error.com
                    if(url.contains("http://dev-apiv2api.upayments.com/get-pay-by-samsung")|| url.contains("https://apiv2api.upayments.com/get-pay-by-samsung")) {

                        showSamsung1(url);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }

        @Override
        @TargetApi(Build.VERSION_CODES.M)
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            final Uri uri = request.getUrl();
            //  showSamsung(uri.toString());
            //   handleError(view, error.getErrorCode(), error.getDescription().toString(), uri);
        }


    }

  private void  showSamsung1(String urlLoad){

        if(!status){

//
            try {
                status= true;
                Intent intent_sendd2 = new Intent(Intent.ACTION_VIEW, Uri.parse(urlLoad));


                someActivityResultLauncher.launch(intent_sendd2);


                // startActivityForResult(intent_sendd2,REQUEST_CODE_PAYMENT);
            } catch (IllegalStateException e) {
                Log.d("Handle","Error"+e.toString());
                // The Samsung Payment Gateway app is not installed on the device
                // Handle this case as needed (e.g., prompt user to install the app)
            }
        }


    }


    private void showSamsung(String urlLoad){

        Intent intent_sendd2 = new Intent(Intent.ACTION_VIEW, Uri.parse(urlLoad));

        try {

//            ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
//                    new ActivityResultContracts.StartActivityForResult(),
//                    new ActivityResultCallback<ActivityResult>() {
//                        @Override
//                        public void onActivityResult(ActivityResult result) {
//                            if (result.getResultCode() == Activity.RESULT_OK) {
//                                // There are no request codes
//                                Intent data = result.getData();
//                                if(data!=null){
//                                    Log.d("HandleSuccess","Success"+data.toString());
//
//                                }
//                                //  doSomeOperations();
//                            }
//                        }
//                    });

         //   someActivityResultLauncher.launch(intent_sendd2);


        } catch (ActivityNotFoundException e) {
            Log.d("Handle","Error"+e.toString());
            // The Samsung Payment Gateway app is not installed on the device
            // Handle this case as needed (e.g., prompt user to install the app)
        }
    }


    private void parseData(final String url) {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i("URL", url);
                try {
//https://apiv2api.upayments.com/get-pay-by-samsung?order_id=o4SH0e022077v41371Vc10O2751Of824&track_id=0252f91u09182192r2050M19260323552223N553&transaction_id=2670134&ref_id=
// 16590634&currency=KWD&amount=0.010&success_url=https://upayments.com/en/&failure_url=https://error.com
                    if(url.contains("http://dev-apiv2api.upayments.com/get-pay-by-samsung") || url.contains("https://apiv2api.upayments.com/get-pay-by-samsung")) {

                       // launchSamsungapp(url);
                     //   showSamsung1(url);

                      //  Toast.makeText(ActivityWeb.this, url, Toast.LENGTH_SHORT).show();
//                        return ;
                    }else{

                    }
                    //

                    if(url.contains("samsung")) {
                     //   Toast.makeText(ActivityWeb.this, url, Toast.LENGTH_SHORT).show();
                     //   launchSamsungapp(url);
                       // return ;
                    }else{

                    }

                    if (url.contains("result") && url.contains("result=CAPTURED")) {

                        UpaymentGatewayLog.d("->  Captured !"+ url.toString(),true);

                        shouldOverride = false;
                        try {
                            String[] split = url.split("\\?");
                            String[] split1 = split[1].split("\\&");

                            Map<String, String> map = new HashMap<>();

                            for (int i = 0; i < split1.length; i++) {
                                String[] split2 = split1[i].split("=");
                                if (split2.length > 1) {
                                    map.put(split2[0].toLowerCase(),
                                            split2[1].replaceAll("%20", " "));
                                }

                            }
                            String payMentId = map.get("payment_id");
                            String Result = map.get("result");
                            String PostDate = map.get("post_date");
                            String TranID = map.get("tran_id");
                            String Ref = map.get("ref");
                            String TrackID = map.get("track_id");
                            String OrderID = map.get("upay_order_id");
                            String cust_ref = map.get("ref");
                            String Auth = map.get("auth");
                            String payment_type= map.get("payment_type");
                            String refund_order_id= map.get("refund_order_id");
                         //   https://upayments.com/en/?payment_id=100328501000019927&
                            //   result=CAPTURED&post_date=1013&tran_id=328501001364179&
                            //   ref=328501001410&track_id=64m57845161224B00Z37973220234274d2i676G6
                            //   &auth=B71256&order_id=3w36Z30v2992L515070wZ9630sIN5923Rf97X308C6D9bn1D5025&requested_order_id=123&
                            //   refund_order_id=3w36Z30v2992L515070wZ9630sIN5923Rf97X308C6D9bn1D5025&payment_type=knet&
                            //   invoice_id=5957050&transaction_date=2023-10-12%2011%3A10%3A12&
                            //   receipt_id=3w36Z30v2992L515070wZ9630sIN5923Rf97X308C6D9bn1D5025

                            PostUpayData postUpayData = new PostUpayData(payMentId, Result, PostDate, TranID
                                    , Ref, TrackID, OrderID, cust_ref, payment_type, Auth,refund_order_id);

                            if (!successPost) {
                                uPaymentCallBack.callBackUpayment(postUpayData);
                                successPost = true;
                            }

                            finish();
                        } catch (Exception e) {
                            e.toString();
                        }
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {

                                finish();
                            }
                        }, 500);
                    } else if (url.contains("result") && url.contains("result=NOT CAPTURED")) {
                        {
                            shouldOverride = true;
                            UpaymentGatewayLog.d("-> Not  Captured !"+ url.toString(),true);


                            String[] split = url.split("\\?");
                            String[] split1 = split[1].split("\\&");

                            //Keys changed to lowercase. value as it is.
                            Map<String, String> map = new HashMap<>();

                            for (int i = 0; i < split1.length; i++) {
                                String[] split2 = split1[i].split("=");
                                if (split2.length > 1) {
                                    map.put(split2[0].toLowerCase(),
                                            split2[1].replaceAll("%20", " "));
                                }

                            }

                            try {
                                DateFormat out = null;
                                String dateconvert = null;
                                SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
                                String dateString = map.get("postdate") +
                                        Calendar.getInstance().get(Calendar.YEAR);
                                Date date = formatter.parse(dateString);
                                out = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                                dateconvert = out.format(date); // 27 Mar 2021
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            String payMentId = map.get("payment_id");
                            String Result = map.get("result");
                            String PostDate = map.get("post_date");
                            String TranID = map.get("tran_id");
                            String Ref = map.get("ref");
                            String TrackID = map.get("track_id");
                            String OrderID = map.get("upay_order_id");
                            String cust_ref = map.get("ref");
                            String Auth = map.get("auth");
                            String payment_type= map.get("payment_type");
                            String refund_order_id= map.get("refund_order_id");

                            PostUpayData postUpayData = new PostUpayData(payMentId, Result, PostDate, TranID
                                    , Ref, TrackID, OrderID, cust_ref, "", Auth,refund_order_id);

                            if (!successPost) {
                                uPaymentCallBack.callBackUpayment(postUpayData);
                                successPost = true;
                            }

                            finish();
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {

                                    finish();
                                    // startActivity(paymentfailure);
                                }
                            }, 500);
                        }
                    }else if(url.contains("result") && url.contains("result=CANCELED"))
                        {
                            shouldOverride = true;
                            UpaymentGatewayLog.d("-> Rejected !"+ url.toString(),true);


                            String[] split = url.split("\\?");
                            String[] split1 = split[1].split("\\&");

                            //Keys changed to lowercase. value as it is.
                            Map<String, String> map = new HashMap<>();

                            for (int i = 0; i < split1.length; i++) {
                                String[] split2 = split1[i].split("=");
                                if (split2.length > 1) {
                                    map.put(split2[0].toLowerCase(),
                                            split2[1].replaceAll("%20", " "));
                                }

                            }

                            try {
                                DateFormat out = null;
                                String dateconvert = null;
                                SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
                                String dateString = map.get("postdate") +
                                        Calendar.getInstance().get(Calendar.YEAR);
                                Date date = formatter.parse(dateString);
                                out = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                                dateconvert = out.format(date); // 27 Mar 2021
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            String payMentId = map.get("payment_id");
                            String Result = map.get("result");
                            String PostDate = map.get("post_date");
                            String TranID = map.get("tran_id");
                            String Ref = map.get("ref");
                            String TrackID = map.get("track_id");
                            String OrderID = map.get("upay_order_id");
                            String cust_ref = map.get("ref");
                            String Auth = map.get("auth");
                            String payment_type= map.get("payment_type");
                            String refund_order_id= map.get("refund_order_id");

                            PostUpayData postUpayData = new PostUpayData(payMentId, Result, PostDate, TranID
                                    , Ref, TrackID, OrderID, cust_ref, payment_type, Auth,refund_order_id);

                            if (!successPost) {
                                uPaymentCallBack.callBackUpayment(postUpayData);
                                successPost = true;
                            }

                            finish();
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {

                                    finish();
                                    // startActivity(paymentfailure);
                                }
                            }, 500);
                        }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 500);
    }

    private void launchSamsungapp(final String url) {



        if(!status){
            try {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {

                        status= true;

                        Uri samsungPayDeepLink = Uri.parse(url);
                        // Create an intent to open the Samsung Pay app using the deep link.
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(samsungPayDeepLink);
                        intent.setPackage("com.samsung.android.spay");
                    }
                }, 500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }

    private void handleError(WebView view, int errorCode, String description, Uri uri) {

        String message = null;
        if (errorCode == WebViewClient.ERROR_AUTHENTICATION) {
            message = "User authentication failed on server";
        } else if (errorCode == WebViewClient.ERROR_TIMEOUT) {
            message = "The server is taking too much time to communicate. Try again later.";
        } else if (errorCode == WebViewClient.ERROR_TOO_MANY_REQUESTS) {
            message = "Too many requests during this load";
        } else if (errorCode == WebViewClient.ERROR_UNKNOWN) {
            message = "Generic error";
        } else if (errorCode == WebViewClient.ERROR_BAD_URL) {
            message = "Check entered URL..";
        } else if (errorCode == WebViewClient.ERROR_CONNECT) {
            message = "Failed to connect to the server";
        } else if (errorCode == WebViewClient.ERROR_FAILED_SSL_HANDSHAKE) {
            message = "Failed to perform SSL handshake";
        } else if (errorCode == WebViewClient.ERROR_HOST_LOOKUP) {
            message = "Server or proxy hostname lookup failed";
        } else if (errorCode == WebViewClient.ERROR_PROXY_AUTHENTICATION) {
            message = "User authentication failed on proxy";
        } else if (errorCode == WebViewClient.ERROR_REDIRECT_LOOP) {
            message = "Too many redirects";
        } else if (errorCode == WebViewClient.ERROR_UNSUPPORTED_AUTH_SCHEME) {
            message = "Unsupported authentication scheme (not basic or digest)";
        } else if (errorCode == WebViewClient.ERROR_UNSUPPORTED_SCHEME) {
            message = "unsupported scheme";
        } else if (errorCode == WebViewClient.ERROR_FILE) {
            message = "Generic file error";
        } else if (errorCode == WebViewClient.ERROR_FILE_NOT_FOUND) {
            message = "File not found";
        } else if (errorCode == WebViewClient.ERROR_IO) {
            message = "The server failed to communicate. Try again later.";
        }
        if (message != null) {
           // Toast.makeText(getActivity(), "" + message, Toast.LENGTH_LONG).show();
            String payMentId=   "cancel";
            String Result=    "CANCELED";
            String PostDate=    "";
            String TranID=    "";
            String Ref=    "";
            String TrackID=   "";
            String OrderID=    "";
            String cust_ref=    "";
            String payment_type= "";
            String Auth = "";
            String  refund_order_id="";

            PostUpayData postUpayData= new PostUpayData(payMentId,Result,PostDate,TranID
                    ,Ref,TrackID,OrderID,cust_ref, payment_type, Auth,refund_order_id);

            uPaymentCallBack.callBackUpayment(postUpayData);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
//        String payMentId=   "cancel";
//        String Result=    "CANCELED";
//        String PostDate=    "";
//        String TranID=    "";
//        String Ref=    "";
//        String TrackID=   "";
//        String OrderID=    "";
//        String cust_ref=    "";
//
//        PostUpayData postUpayData= new PostUpayData(payMentId,Result,PostDate,TranID
//                ,Ref,TrackID,OrderID,cust_ref);
//
//        uPaymentCallBack.callBackUpayment(postUpayData);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
           // this.webView.goBack();
            webView.goBack();
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


}
