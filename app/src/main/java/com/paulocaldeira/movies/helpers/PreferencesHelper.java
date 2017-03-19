package com.paulocaldeira.movies.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Preferences
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 3/19/17
 */

public class PreferencesHelper {
    // Preferences
    public static final String PREF_MOVIES_LIST_MODE = "movies_list_mode";

    /**
     * Returns movies list mode
     * @param context Context
     * @return Movies list mode
     */
    public static String getMoviesListMode(Context context) {
        return getMoviesListMode(context, null);
    }

    /**
     * Returns movies list mode
     * @param context Context
     * @param defaultValue Default value
     * @return Movies list mode
     */
    public static String getMoviesListMode(Context context, String defaultValue) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(PREF_MOVIES_LIST_MODE, defaultValue);
    }

    /**
     * Sets movies list mode
     * @param context Context
     * @param mode Default value
     * @return Movies list mode
     */
    public static void setMoviesListMode(Context context, String mode) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_MOVIES_LIST_MODE, mode);
        editor.commit();
    }

    /**
     * Register on shared preferences change listener
     * @param listener Listener
     */
    public static void registerUserSharedPreferencesListener(Context context, SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPref.registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Unregister on shared preferences change listener
     * @param listener Listener
     */
    public static void unregisterUserSharedPreferencesListener(Context context, SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPref.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
