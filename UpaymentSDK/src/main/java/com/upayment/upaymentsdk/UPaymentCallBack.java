package com.upayment.upaymentsdk;

//import com.upayments.activity.PostUpayData;

import com.upayment.upaymentsdk.activity.PostUpayData;
import com.upayment.upaymentsdk.track.CreateInvoiceResponse;
import com.upayment.upaymentsdk.track.card.addcard.ResponseAddCard;
import com.upayment.upaymentsdk.track.card.retrive.ResponseRetriveCard;
import com.upayment.upaymentsdk.track.refund.SingleDeleteRefundResponse;
import com.upayment.upaymentsdk.track.refund.SingleRefundResponse;
import com.upayment.upaymentsdk.track.refund.multidelete.ResponseMultiRefundDelete;
import com.upayment.upaymentsdk.track.refund.multirefund.ResponseMultivendorRefund;
import java.io.Serializable;

public interface UPaymentCallBack extends Serializable {

   void callBackUpayment(PostUpayData postUpayData);
   void errorPayUpayment(String data);

   void sucessCreateInvoice(CreateInvoiceResponse invoiceResponse);

   void sucessMultiRefundDelete(ResponseMultiRefundDelete invoiceResponse);
   void sucessAddCard(ResponseAddCard invoiceResponse);

   void sucessSingleRefund(SingleRefundResponse invoiceResponse);
   void sucessSingleDeleteRefund(SingleDeleteRefundResponse invoiceResponse);

//   void sucessMultiDeleteRefund(sucessMultiDeleteRefund invoiceResponse);


   void sucessMultiRefund(ResponseMultivendorRefund invoiceResponse);

   void sucessRetriveCard(ResponseRetriveCard invoiceResponse);

   void failureMultiRefund(ResponseMultivendorRefund invoiceResponse);


}
