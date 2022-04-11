package com.project.wecare.helpers;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

public class LocaleHelper {
    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";
    public static Context context;

    public static Context setLocale(Context context, String language) {
        persist(context, language);

        // updating the language for devices above android nougat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        }
        // for devices having lower version of android os
        return updateResourcesLegacy(context, language);
    }

    private static void persist(Context context, String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SELECTED_LANGUAGE, language);
        editor.apply();
    }

    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }

//    // the method is used to set the language at runtime
//    public static void setLocale(Context context, String language) {
//        persist(context, language);
//        LocaleHelper.context = context;
//
//        // updating the language for devices above android nougat
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            updateResources(language);
//        } else {
//            // for devices having lower version of android os
//            updateResourcesLegacy(language);
//        }
//    }
//
//    private static void persist(Context context, String language) {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(SELECTED_LANGUAGE, language);
//        editor.apply();
//    }
//
    public static String retrieve(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SELECTED_LANGUAGE, "en");
    }
//
//    // the method is used update the language of application by creating
//    // object of inbuilt Locale class and passing language argument to it
//    @TargetApi(Build.VERSION_CODES.N)
//    private static void updateResources(String language) {
//        Locale locale = new Locale(language);
//        Locale.setDefault(locale);
//
//        Configuration configuration = LocaleHelper.context.getResources().getConfiguration();
//        configuration.setLocale(locale);
//        configuration.setLayoutDirection(locale);
//
//        LocaleHelper.context.createConfigurationContext(configuration);
//    }
//
//
//    private static void updateResourcesLegacy(String language) {
//        Locale locale = new Locale(language);
//        Locale.setDefault(locale);
//
//        Resources resources = LocaleHelper.context.getResources();
//
//        Configuration configuration = resources.getConfiguration();
//        configuration.locale = locale;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            configuration.setLayoutDirection(locale);
//        }
//
//        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
//    }
}
