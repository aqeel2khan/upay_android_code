package com.upayment.upaymentsdk.track;

/**
 * Created by Adil on 28/03/2015.
 */
class UpaymentGatewayConfig {

    static final long MAX_UNZIPPED_BYTES_PER_SEND = 100000;
    static final long POST_FAILED_RETRY_DELAY_MILLIS = 5000;
    static final long NO_INTERNET_RETRY_DELAY_MILLIS = 60000;

    public static Long generateNumber(){
        Long tsLong = System.currentTimeMillis()/1000;

        return tsLong;
    }
}
