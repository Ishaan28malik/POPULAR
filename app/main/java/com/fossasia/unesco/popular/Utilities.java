package com.fossasia.unesco.popular;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utilities {

    private Utilities() {
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    } // ... Resize image for better UI

    public static Drawable getResourcesDrawable(String name, Context context) {
        int id = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        return context.getResources().getDrawable(id);
    } // ... Get resources drawable from string id

    public static String getResourcesString(String name, Context context) {
        int id = context.getResources().getIdentifier(name, "string", context.getPackageName());
        return context.getResources().getString(id);
    } // ... Get resources string from string id

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) view = new View(activity);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    } // ... hide android soft keyboard
}
