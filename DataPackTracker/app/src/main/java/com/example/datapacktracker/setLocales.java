package com.example.datapacktracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

public class setLocales {

    public static Context setLocale(Context context) {
        return setAccordingtoSDK(context, getString(context, Locale.getDefault().getLanguage()));
    }

    public static Context setLocaleUtil(Context context, String str) {
        return setAccordingtoSDK(context, getString(context, str));
    }

    public static Context setAccordingtoSDK(Context context, String str) {
        setPreference(context, str);
        return Build.VERSION.SDK_INT >= 24 ? setForSDKAbove24(context, str) : setForSDKBelow24(context, str);
    }

    private static String getString(Context context, String str) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("pref_select_language", str);
    }

    private static void setPreference(Context context, String str) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putString("pref_select_language", str);
        edit.apply();
    }

    private static Context setForSDKAbove24(Context context, String str) {
        Locale locale = new Locale(str);
        Locale.setDefault(locale);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        return context.createConfigurationContext(configuration);
    }

    private static Context setForSDKBelow24(Context context, String str) {
        Locale locale = new Locale(str);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        configuration.setLayoutDirection(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }
}
