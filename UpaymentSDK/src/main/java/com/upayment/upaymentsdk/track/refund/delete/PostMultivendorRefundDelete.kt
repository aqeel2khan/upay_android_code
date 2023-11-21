package com.upayment.upaymentsdk.track.refund.delete

data class PostMultivendorRefundDelete(
    var generatedInvoiceId: String,
    var orderId: String,
    var refundOrderId: String,
    var refundArn: String
)



