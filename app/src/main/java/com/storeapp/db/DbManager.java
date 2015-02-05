package com.storeapp.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.storeapp.AppContextProvider;

public class DbManager extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "storeapp.db";
	public static final int DATABASE_VERSION = 1;

	private static DbManager dbManager = new DbManager();

	private UsersManager usersManager;
	private ItemsManager itemsManager;
	private CommunicationManager communicationManager;
	private CartManager cartManager;

	private DbManager() {
		super(AppContextProvider.getContext(), DATABASE_NAME, null,
				DATABASE_VERSION);

		this.usersManager = new UsersManager(this);
		this.itemsManager = new ItemsManager(this);
		this.communicationManager = new CommunicationManager(this);
		this.cartManager = new CartManager(this);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		usersManager.onCreate(db);
		itemsManager.onCreate(db);
		communicationManager.onCreate(db);
		cartManager.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		usersManager.onUpgrade(db, oldVersion, newVersion);
		itemsManager.onUpgrade(db, oldVersion, newVersion);
		communicationManager.onUpgrade(db, oldVersion, newVersion);
		cartManager.onUpgrade(db, oldVersion, newVersion);

	}

	public static DbManager getDbManager() {
		return dbManager;
	}

	public UsersManager getUserManager() {
		return usersManager;
	}

	public ItemsManager getItemsManager() {
		return itemsManager;
	}

	public CommunicationManager getCommunicationManager() {
		return communicationManager;
	}

	public CartManager getCartManager() {
		return cartManager;
	}
}
