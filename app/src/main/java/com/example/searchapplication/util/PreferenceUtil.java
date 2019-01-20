package com.example.searchapplication.util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class PreferenceUtil {

    public static void setArgDate(Context context, String argDate) {
        SharedPreferences pref = context.getSharedPreferences("argDate", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("argDate", argDate);
        editor.commit();
    }

    public static String getArgDate(Context context) {
        SharedPreferences pref = context.getSharedPreferences("argDate", MODE_PRIVATE);
        return pref.getString("argDate", "");
    }

    public static void setArgThumbnailImg(Context context, String argThumbnailImg) {
        SharedPreferences pref = context.getSharedPreferences("argThumbnailImg", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("argThumbnailImg", argThumbnailImg);
        editor.commit();
    }

    public static String getArgThumbnailImg(Context context) {
        SharedPreferences pref = context.getSharedPreferences("argThumbnailImg", MODE_PRIVATE);
        return pref.getString("argThumbnailImg", "");
    }
}
