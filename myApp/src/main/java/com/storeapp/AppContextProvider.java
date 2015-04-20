package com.storeapp;

import android.app.Application;
import android.content.Context;

import com.storeapp.parse.ParseInitializer;

public class AppContextProvider extends Application {

	private static Context ctx;

	@Override
	public void onCreate() {
		super.onCreate();

		ctx = getApplicationContext();

        ParseInitializer.initialize(ctx);
    }

	public static Context getContext() {

		return ctx;
	}

}
