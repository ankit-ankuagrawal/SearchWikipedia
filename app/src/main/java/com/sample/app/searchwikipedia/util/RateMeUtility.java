package com.sample.app.searchwikipedia.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;


public class RateMeUtility {
    public static void rateMe(Context context) {
        String packageName = context.getApplicationContext().getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(AppConstants.PRE_OPEN_PLAY_STORE_APP_URL + packageName)));
        } catch (android.content.ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(AppConstants.PRE_GOOGLE_PLAY_APP_URL + packageName)));
        }
    }
}
