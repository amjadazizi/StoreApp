package com.storeapp.Activities;

import android.app.Application;
import android.content.Context;

import com.storeapp.Activities.parse.ParseInitializer;

public class AppContextProvider extends Application {

	private static Context ctx;

	@Override
	public void onCreate() {
		super.onCreate();

		ctx = getApplicationContext();

		//Prefs.changeUserLanguageToCurrent();

        ParseInitializer.initialize(ctx);
    }

	public static Context getContext() {

		return ctx;
	}

}
