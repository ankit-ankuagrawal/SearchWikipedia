package com.appsnesst.searchwikipedia.listener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationView;
import com.sample.app.searchwikipedia.R;
import com.appsnesst.searchwikipedia.util.NavigationDrawerUtil;

public class NavigationDrawerListener implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final String LOG_TAG = NavigationDrawerListener.class.getSimpleName();

    private Context context;

    public NavigationDrawerListener(Context context) {
        this.context = context;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mmAboutAppsNesst:
                new AlertDialog.Builder(context)
                        .setTitle(R.string.dev_apps_neest_home_for_software_development)
                        .setMessage(R.string.dev_about_apps_nesst_detail)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                return true;
            case R.id.mmOtherAppsFromDeveloper:
                NavigationDrawerUtil.otherAppsFromDeveloper(context);
                return true;
            case R.id.mmContactDeveloper:
                View dialogView = LayoutInflater.from(context).inflate(R.layout.dev_app_developer_contact_us, null);
                dialogView.findViewById(R.id.devCall).setOnClickListener(this);
                dialogView.findViewById(R.id.devEmailId).setOnClickListener(this);

                new AlertDialog.Builder(context)
                        .setTitle(R.string.dev_contact_details)
                        .setView(dialogView)
                        //.setMessage(R.string.dev_contact_apps_nesst)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                return true;
            case R.id.mmAppRate:
                NavigationDrawerUtil.rateMe(context);
                return true;
            case R.id.mmAppShare:
                NavigationDrawerUtil.shareTheApp(context);
                return true;
            case R.id.mmAppExit:
                if (context instanceof Activity)
                    ((Activity) context).finishAffinity();
                return true;
            default:
                return false;
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.devCall:
                NavigationDrawerUtil.call(context, context.getString(R.string.contact_no));
                break;
            case R.id.devEmailId:
                NavigationDrawerUtil.sendEmailId(context, context.getString(R.string.email_id));
                break;
        }
    }
}
