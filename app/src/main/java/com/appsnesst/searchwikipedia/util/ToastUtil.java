package com.appsnesst.searchwikipedia.util;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.sample.app.searchwikipedia.databinding.RowToastBinding;

public class ToastUtil {

    public static void showToast(Context activityContext, int image, String message, int backgroundColor, int textColor, int imageColor) {
        if (activityContext != null) {

            RowToastBinding rowToastBinding = RowToastBinding.inflate(LayoutInflater.from(activityContext));
            if (image > 0) {
                rowToastBinding.img.setImageResource(image);
            }

            if (message != null) {
                rowToastBinding.tvMessage.setText(message);
            }

            rowToastBinding.tvMessage.setTextColor(activityContext.getResources().getColor(textColor));

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                rowToastBinding.rlMain.setBackgroundTintList(activityContext.getResources().getColorStateList(backgroundColor));
            } else {
                ViewCompat.setBackgroundTintList(rowToastBinding.rlMain, activityContext.getResources().getColorStateList(backgroundColor));
            }

            rowToastBinding.img.setColorFilter(ContextCompat.getColor(activityContext, imageColor));


            Toast toast = new Toast(activityContext);

            toast.setView(rowToastBinding.getRoot());
            toast.setDuration(Toast.LENGTH_SHORT);

            toast.show();
        }
    }
}
