package com.upayment.upaymentsdk.activity;

import android.location.Address;
import android.view.View;


import com.upayment.upaymentsdk.UPaymentCallBack;
import com.upayment.upaymentsdk.track.CreateInvoiceResponse;
import com.upayment.upaymentsdk.track.UpaymentGateway;
import com.upayment.upaymentsdk.track.UpaymentGatewayAction;
import com.upayment.upaymentsdk.track.UpaymentGatewayCentricProperty;
import com.upayment.upaymentsdk.track.UpaymentGatewayEvent;
import com.upayment.upaymentsdk.track.card.addcard.ResponseAddCard;
import com.upayment.upaymentsdk.track.card.retrive.ResponseRetriveCard;
import com.upayment.upaymentsdk.track.refund.SingleDeleteRefundResponse;
import com.upayment.upaymentsdk.track.refund.SingleRefundResponse;
import com.upayment.upaymentsdk.track.refund.multidelete.ResponseMultiRefundDelete;
import com.upayment.upaymentsdk.track.refund.multirefund.ResponseMultivendorRefund;
import com.upayment.upaymentsdk.util.UpaymentGatewayAppUtils;
import java.util.List;

/**
 * Created by Adil on 09/03/17.
 */

public class Test implements UPaymentCallBack {


    private static final String TAG = Test.class.getSimpleName();

    @Override
    public void callBackUpayment(final PostUpayData postUpayData) {

    }

    @Override
    public void errorPayUpayment(final String data) {

    }

    @Override
    public void sucessCreateInvoice(final CreateInvoiceResponse invoiceResponse) {

    }

    @Override
    public void sucessMultiRefundDelete(final ResponseMultiRefundDelete invoiceResponse) {

    }

    @Override
    public void sucessAddCard(final ResponseAddCard invoiceResponse) {

    }

    @Override
    public void sucessSingleRefund(final SingleRefundResponse invoiceResponse) {

    }

    @Override
    public void sucessSingleDeleteRefund(final SingleDeleteRefundResponse invoiceResponse) {

    }

    @Override
    public void sucessMultiRefund(final ResponseMultivendorRefund invoiceResponse) {

    }

    @Override
    public void sucessRetriveCard(final ResponseRetriveCard invoiceResponse) {

    }

    @Override
    public void failureMultiRefund(final ResponseMultivendorRefund invoiceResponse) {

    }

    // DOC How to use SDK
    // Key as shared to you
//          In Your Application class inside onCreate innitialize

//          Step1)  AdosizAnalytics.init(this,"key","pub_id",true);

//          if You doesnt have Application class then create on Application class and inside on create method

//          do Step1)   AdosizAnalytics.init(this,"key","pub_id",true);

//          step 2) Deaclre that application class inside Manifest under Application tag

//            Inside Manifest  under Application  declare Receiver

//         step 3)<p>If you plan to use multiple install refer, please refer to this workaround: &nbsp;https://mixpanel

//            * .com/help/questions/articles/how-can-i-use-multiple-install-trackers-with-the-android-library&nbsp;</p>
//            * <p>Once you've added the &lt;receiver&gt; tag to your manifest,
//            * the first call to {@link com.adosis.mobo.track( UpaymentGatewayEvent UpaymentGatewayEvent )}
//            * will include the user's Google Play Referrer as metadata. In addition, if
//            * you include utm parameters in your link to Google Play, they will be parsed and
//            * provided as individual properties in your track calls.</p>

//
//          a) If you doesnt have any install Receiver then decalre this
//            <receiver android:name="com.adosis.mobo.track.UpaymentGatewayInstallRefReceiver">
//            <intent-filter>
//            <action android:name="com.android.vending.INSTALL_REFERRER" />
//            </intent-filter>
//            </receiver>
//
//           b) If you are using multiple install referer then follow link
//
//            https://mixpanel.com/help/questions/articles/how-can-i-use-multiple-install-trackers-with-the-android-library
//
//            You have to make Your own class extend by BroadcastReceiver register this class inside Manifest
//
//            Inside Your Broadcast Receiver class
//
//            Innitialize  this
//            UpaymentGatewayInstallRefReceiver adosizReceiver= new UpaymentGatewayInstallRefReceiver();
 //          adosizReceiver.onReceive(context, intent);




    public void onEventTrack(View v){

//        UpaymentGatewayEvent genericTag = new UpaymentGatewayEvent.Builder("")//compulsary
//                .setEvent(UpaymentGatewayAppUtils.OPEN) // compulsary
//              //  .setUserId("2")// optional
//                .setUID("5")
//                .setOrderId("1")// optional
//                .setOrderValue("12")// optional
//                .build();
     //   UpaymentGateway.getInstance().track(genericTag, this);
    }


    public void onClickProperties(View v) {
//        UpaymentGatewayEvent properties = new UpaymentGatewayEvent.Builder("the_path")
//                .setNewCustomer(true)
//                .setEmail("test-email")
//                .setPageGroup("test-group")
//              //  .setLocation(latitude, longitude)
//                .setProfile("test-profile")
//                .setUID("test-uid")
//                .set("whatever", "...")
//                .set("whatever1", "...")
//                .set("whatever2", "...")
//                .setAction(new UpaymentGatewayAction.Builder()
//                        .setReference("test-ref-\"fefds$432`^")
//                        .setIn("in-test")
//                        .addOut(new String[]{"tata", "tutu", "tete"})
//                        .build())
//                .setProperty(new UpaymentGatewayCentricProperty.Builder()
//                        .set("cle1", new String[]{"poisson", "viande"})
//                        .set("cle2", "choucroute")
//                        .build())
//                .build();
//      UpaymentGateway.getInstance().track(properties, this);
    }



    public void onClickSearch(View v) {


//        UpaymentGatewayEvent analyticsEvent2 = new UpaymentGatewayEvent.Builder("1201")
//                    //.setEvent("1201")
//                    .setMerchantId("1201")
//                    .setUsername("test")
//                    .setPassword("test")
//                    .setApikey("jtest123")
//                    .setOrderId("12345")
//                    .setTotalPrice("3.000")
//                   .setCurrencyCode("NA")
//                   .setSuccessUrl("https://example.com/success.html")
//                   .setErrorUrl("https://example.com/error.html")
//                  .setTestMode("1")
//                  .setCustomerName("")
//                  .setCustomerEmail("")
//                 .setCustomerMobile("")
//                 .setPaymentGateway("")
//                 .setWhitelabled(true)
//                 .setReference("")
//                    //.setUserId("1")
//                    .build();
       // UpaymentGateway.getInstance().track(analyticsEvent2,this);
    }




    public interface OnGeocoderFinishedListener {
        public abstract void onFinished(List<Address> results);
    }
}
