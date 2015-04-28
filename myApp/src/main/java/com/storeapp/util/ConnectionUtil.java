package com.storeapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Amjad on 26-04-2015.
 */
public class ConnectionUtil {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;


    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static int getConnectivityStatusInt(Context context) {
        int conn = ConnectionUtil.getConnectivityStatus(context);
        int status = -1;
        if (conn == ConnectionUtil.TYPE_WIFI) {
            status = TYPE_WIFI;   //"Wifi enabled";
        } else if (conn == ConnectionUtil.TYPE_MOBILE) {
            status = TYPE_MOBILE; //"Mobile data enabled";
        } else if (conn == ConnectionUtil.TYPE_NOT_CONNECTED) {
            status = TYPE_NOT_CONNECTED ;//"No Internet Access";
        }
        return status;
    }
}
