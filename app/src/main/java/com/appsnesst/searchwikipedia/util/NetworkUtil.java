package com.appsnesst.searchwikipedia.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sample.app.searchwikipedia.R;

public class NetworkUtil {

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork!=null && activeNetwork.isConnectedOrConnecting()){
            return true;
        }else{
            ToastUtil.showToast(context,
                    R.drawable.netrowk_off,context.getString(R.string.you_are_offline_please_check_your_internet_connection),android.R.color.black,R.color.white,R.color.white);
            return  false;
        }
    }
}
