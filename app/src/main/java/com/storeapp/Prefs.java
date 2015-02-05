package com.storeapp;

import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;

public class Prefs {
	public static final String KEY_IS_USER_LOGGED_IN = "pref_is_user_logged_in";
	public static final String KEY_INITIALS = "pref_initials";
	public static final String KEY_CHANGE_LANGUAGES = "pref_change_language";
	public static final String PREF_LANGUAGE_CHANGED = "pref_language_changed";
	public static final String prefRecreateKey = "reCreate_Key";
	public static final String prefRecreatingKey = "reCreating_Key";

	private static Locale locale = null;

	public static SharedPreferences getPrefs() {
		Context context = AppContextProvider.getContext();

		return PreferenceManager.getDefaultSharedPreferences(context);

	}

	public static void setStringPref(String key, String value) {

		SharedPreferences prefs = getPrefs();
		Editor editor = prefs.edit();

		editor.putString(key, value);
		editor.commit();

	}

	public static String getStringPref(String key, String defValue) {
		SharedPreferences prefs = getPrefs();
		return prefs.getString(key, defValue);
	}

	public static String getInitialsPref() {

		SharedPreferences prefs = getPrefs();

		return prefs.getString(KEY_INITIALS, null);
	}

	public static boolean showInitialsPref() {

		Context context = AppContextProvider.getContext();
		String key = context.getString(R.string.pref_key_show_initials);
		boolean defValue = context.getResources().getBoolean(
				R.bool.pref_key_show_initials_default);

		SharedPreferences prefs = getPrefs();

		return prefs.getBoolean(key, defValue);
	}

	public static boolean isUserLoggedIn() {

		SharedPreferences prefs = getPrefs();
		return prefs.getBoolean(KEY_IS_USER_LOGGED_IN, false);
	}

	public static void setUserLoggedIn(boolean loggedIn) {

		SharedPreferences prefs = getPrefs();
		Editor editor = prefs.edit();
		editor.putBoolean(KEY_IS_USER_LOGGED_IN, loggedIn);
		editor.commit();
	}

	public static boolean isDeletingOldMessagesEnabled() {

		Context context = AppContextProvider.getContext();
		String isEnabledKey = context
				.getString(R.string.pref_key_should_delete_old_messages);
		SharedPreferences prefs = getPrefs();
		boolean defValue = context.getResources().getBoolean(
				R.bool.pref_key_should_delete_old_messages_default);

		return prefs.getBoolean(isEnabledKey, defValue);
	}

	public static int getDeletingOldMessagesDays() {

		Context context = AppContextProvider.getContext();
		String key = context.getString(R.string.pref_key_delete_messages);
		SharedPreferences prefs = getPrefs();
		int defValue = Integer.parseInt(context.getResources().getString(
				R.string.value_delete_messages_1_year_old));

		String days = prefs.getString(key, null);
		int daysvalue = days == null ? defValue : Integer.parseInt(days);
		return daysvalue;
	}

	public static boolean isSoundsEnabledPref() {

		Context context = AppContextProvider.getContext();
		String key = context
				.getString(R.string.pref_key_enable_application_sounds);
		boolean defValue = context.getResources().getBoolean(
				R.bool.pref_key_enable_application_sounds_default);

		SharedPreferences prefs = getPrefs();

		return prefs.getBoolean(key, defValue);
	}

	public static String getCurrentUserLanguage() {
		Context context = AppContextProvider.getContext();
		String key = context
				.getString(R.string.pref_key_change_application_language);
		String defValue = context.getString(R.string.value_language_default);

		SharedPreferences prefs = getPrefs();
		String language = prefs.getString(key, null);
		String languageValue = language == null ? defValue : language;
		return languageValue;

	}

	public static void changeUserLanguageToCurrent() {
		String languageCode = getCurrentUserLanguage();
		changeUserLanguage(languageCode);
	}

	public static void changeUserLanguage(String languageCode) {

		// get selected language and create locale

		locale = new Locale(languageCode);
		Context ctx = AppContextProvider.getContext();
		Resources res = ctx.getResources();
		// Change locale settings in the app.

		Locale.setDefault(locale);
		Configuration conf = res.getConfiguration();
		conf.locale = locale;
		res.updateConfiguration(conf, ctx.getResources().getDisplayMetrics());
	}
}
