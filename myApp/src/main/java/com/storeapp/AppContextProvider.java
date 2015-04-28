package com.storeapp;

import android.app.Application;
import android.content.Context;

import com.storeapp.parse.ParseInitializer;
import com.storeapp.util.ConnectionUtil;
import com.storeapp.util.SweetAlerts;

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
