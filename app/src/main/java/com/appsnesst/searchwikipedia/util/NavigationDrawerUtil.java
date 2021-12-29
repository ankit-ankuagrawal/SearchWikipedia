package com.appsnesst.searchwikipedia.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;

import com.sample.app.searchwikipedia.BuildConfig;
import com.sample.app.searchwikipedia.R;

public class NavigationDrawerUtil {

    private static final String PRE_GOOGLE_PLAY_APP_URL = "https://play.google.com/store/apps/details?id=";
    private static final String PRE_OPEN_PLAY_STORE_APP_URL = "market://details?id=";

    private static final String PRE_GOOGLE_APP_DEVELOPER_URL = "https://play.google.com/store/apps/developer?id=";
    private static final String DEVELOPER_ID = "Apps+Nesst";

    public static void updateNavigationMenu(Menu menu)
    {
        MenuItem mmAppVersion = menu.findItem(R.id.mmAppVersion);
        if(mmAppVersion!=null)
        {
            mmAppVersion.setTitle("APP VERSION"+" : "+ BuildConfig.VERSION_NAME);
        }
    }

    public static void otherAppsFromDeveloper(Context context)
    {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PRE_GOOGLE_APP_DEVELOPER_URL+DEVELOPER_ID)));
    }

    public static void rateMe(Context context) {
        String packageName = context.getApplicationContext().getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(PRE_OPEN_PLAY_STORE_APP_URL + packageName)));
        } catch (android.content.ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(PRE_GOOGLE_PLAY_APP_URL + packageName)));
        }
    }

    public static void shareTheApp(Context context) {

        String appUrl = PRE_GOOGLE_PLAY_APP_URL + context.getApplicationContext().getPackageName();
        String extraString = context.getString(R.string.dev_install_the_app, context.getString(R.string.app_name), appUrl);
        String titleString = context.getString(R.string.dev_app_share);
        share(context, extraString, titleString);

    }

    private static void share(Context context, String extraString, String titleString) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, extraString);
        context.startActivity(Intent.createChooser(intent, titleString));
    }

    public static void call(Context context, String contactNo)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+contactNo));
        context.startActivity(intent);
    }

    public static void sendEmailId(Context context, String emailIdTo)
    {
        Intent intent = new Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:")).putExtra(Intent.EXTRA_EMAIL, new String[]{emailIdTo});
        context.startActivity(intent);
    }
}
