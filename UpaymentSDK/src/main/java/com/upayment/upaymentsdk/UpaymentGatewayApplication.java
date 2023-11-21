package com.upayment.upaymentsdk;

import android.app.Application;
import com.upayment.upaymentsdk.track.UpaymentGateway;


/**
 * Created by Adil
 */
public class UpaymentGatewayApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
     UpaymentGateway.init(this,"","",true);
    }
}
