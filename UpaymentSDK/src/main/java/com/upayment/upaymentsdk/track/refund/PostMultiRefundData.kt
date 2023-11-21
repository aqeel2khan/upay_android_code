package com.upayment.upaymentsdk.track.refund

class PostMultiRefundData {
    var orderId: String=""
   var  receiptId:String= ""
    var customerFirstName:String =""
    var customerEmail: String= ""
   var customerMobileNumber:String= ""
    var reference:String= ""
    var notifyUrl:String= ""
    var refundPayload:  MutableList<RefundPayload> = arrayListOf<RefundPayload>()

}