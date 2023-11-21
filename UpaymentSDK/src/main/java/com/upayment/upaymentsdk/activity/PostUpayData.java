package com.upayment.upaymentsdk.activity;

public class PostUpayData {

    String payMentId= "";
    String Result=   "";
    String PostDate=  "";
    String TranID=   "";
    String Ref=    "";
    String TrackID=   "";
    String OrderID=   "";
    String cust_ref=    "";

    public String getPayment_type() {
        return payment_type;
    }

    String payment_type="";
    String auth="";

    public String getRefund_order_id() {
        return refund_order_id;
    }

    String refund_order_id="";

    public String getAuth() {
        return auth;
    }

    public PostUpayData(String payMentId, String result, String postDate, String tranID, String ref, String trackID,
            String OrderID, String cust_ref, String payment_type, final String auth,final String refund_order_id) {

        this.payMentId= payMentId;
        this.Result= result;
        this.PostDate= postDate;
        this.TranID= tranID;
        this.Ref= ref;
        this.TrackID= trackID;
        this.OrderID= OrderID;
        this.cust_ref= cust_ref;
        this.payment_type= payment_type;
        this.auth=auth;
        this.refund_order_id= refund_order_id;
    }

    public String getPayMentId() {
        return payMentId;
    }

    public String getResult() {
        return Result;
    }

    public String getPostDate() {
        return PostDate;
    }

    public String getTranID() {
        return TranID;
    }

    public String getRef() {
        return Ref;
    }

    public String getTrackID() {
        return TrackID;
    }

    public String getOrderID() {
        return OrderID;
    }

    public String getCust_ref() {
        return cust_ref;
    }
}
