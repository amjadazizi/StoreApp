package com.storeapp.Db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.storeapp.AppContextProvider;

public class DbManager extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "storeapp.db";
	public static final int DATABASE_VERSION = 1;

	private static DbManager dbManager = new DbManager();


	private CartItemsManager cartItemsManager;

	private DbManager() {
		super(AppContextProvider.getContext(), DATABASE_NAME, null,
				DATABASE_VERSION);


		this.cartItemsManager = new CartItemsManager(this);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		cartItemsManager.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		cartItemsManager.onUpgrade(db, oldVersion, newVersion);

	}

	public static DbManager getDbManager() {
		return dbManager;
	}



	public CartItemsManager getCartItemsManager() {
		return cartItemsManager;
	}
}
