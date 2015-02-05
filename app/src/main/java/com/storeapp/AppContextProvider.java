package com.storeapp;

import com.storeapp.util.Utils;

import android.app.Application;
import android.content.Context;

public class AppContextProvider extends Application {

	private static Context ctx;

	@Override
	public void onCreate() {
		super.onCreate();

		ctx = getApplicationContext();

		Prefs.changeUserLanguageToCurrent();

	}

	public static Context getContext() {

		return ctx;
	}

}
