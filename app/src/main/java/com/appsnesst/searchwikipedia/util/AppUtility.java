package com.appsnesst.searchwikipedia.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.inputmethod.InputMethodManager;

import com.appsnesst.searchwikipedia.MyApplication;

import java.util.Locale;

public class AppUtility {

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static Locale getCurrentLocale(){
        Context context = MyApplication.getContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return context.getResources().getConfiguration().getLocales().get(0);
        } else{
            //noinspection deprecation
            return context.getResources().getConfiguration().locale;
        }
    }
}
