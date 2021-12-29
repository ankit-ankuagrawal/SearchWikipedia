package com.appsnesst.searchwikipedia.util;

import android.content.Context;
import android.content.Intent;

import com.sample.app.searchwikipedia.R;

public class ShareUtility {

    public static void shareTheApp(Context context) {

        String appUrl = AppConstants.PRE_GOOGLE_PLAY_APP_URL + context.getApplicationContext().getPackageName();
        String extraString = context.getString(R.string.install_the_app, context.getString(R.string.app_name), appUrl);
        String titleString = context.getString(R.string.share_app);
        share(context, extraString, titleString);

    }

    /*public static void shareTheContent(Context context, String extraString) {
        share(context, extraString, context.getString(R.string.share_content));
    }*/

    private static void share(Context context, String extraString, String titleString) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, extraString);
        context.startActivity(Intent.createChooser(intent, titleString));
    }
}
