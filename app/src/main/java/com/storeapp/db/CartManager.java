package com.storeapp.db;

import com.storeapp.util.Utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class CartManager {

    //Changeds

	public static final String TABLE_NAME = "tbl_cart";
	public static final String COL_ID = BaseColumns._ID;
	public static final String COL_ITEM_ID = "col_item_id";
	public static final String COL_AMOUNT = "col_amount";

	private SQLiteOpenHelper dbHelper;
	private ContentValues values;

	public CartManager(SQLiteOpenHelper dbHelper) {

		this.dbHelper = dbHelper;
		this.values = new ContentValues();

	}

	public void onCreate(SQLiteDatabase db) {

		String sql = String
				.format("create table %s(%s integer primary key autoincrement, %s integer not null, %s integer not null)",
						TABLE_NAME, COL_ID, COL_ITEM_ID, COL_AMOUNT);

		db.execSQL(sql);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public int addItemToCart(String barcode, int amount) {

		int rowId = -1;
		SQLiteDatabase db = null;
		int itemId = DbManager.getDbManager().getItemsManager()
				.getItemId(barcode);

		try {

			values.clear();
			values.put(COL_ITEM_ID, itemId);
			values.put(COL_AMOUNT, amount);
			db = dbHelper.getWritableDatabase();

			rowId = (int) db.insert(TABLE_NAME, null, values);

		} catch (Exception e) {
			String msg = e.getMessage();
		} finally {
			Utils.closeDb(db);
		}

		return rowId;
	}

	public void clearCart() {

		SQLiteDatabase db = null;

		try {

			String sql = "delete from " + TABLE_NAME;
			db = dbHelper.getWritableDatabase();

			db.execSQL(sql);

		} finally {
			Utils.closeDb(db);
		}
	}

	public Cursor getItemsInCart() {
		Cursor cursor = null;
		SQLiteDatabase db = null;
		int count = -1;
		try {

			String sql = String.format(
					"select * from %s a, %s b where a.%s = b.%s",
					ItemsManager.TABLE_NAME, TABLE_NAME, ItemsManager.COL_ID,
					COL_ITEM_ID);

			db = dbHelper.getReadableDatabase();
			cursor = db.rawQuery(sql, null);

			count = cursor.getCount();

		} catch (Exception e) {
			String msg = e.getMessage();
		} finally {
		}

		return cursor;

	}

	public int getItmCount(String barcode) {
		int itmCount = 0;
		Cursor cursor = null;
		SQLiteDatabase db = null;

		int itemId = DbManager.getDbManager().getItemsManager()
				.getItemId(barcode);

		try {

			String where = COL_ITEM_ID + " = " + itemId;

			db = dbHelper.getReadableDatabase();
			String[] columns = { COL_AMOUNT };
			cursor = db.query(TABLE_NAME, columns, where, null, null, null,
					null);
			if (cursor.moveToNext()) {
				int colmIndex = cursor.getColumnIndex(COL_AMOUNT);
				itmCount = cursor.getInt(colmIndex);
			}

		} catch (Exception e) {
			String msg = e.getMessage();
		} finally {
			Utils.closeCursor(cursor);
			Utils.closeDb(db);

		}

		return itmCount;
	}

	public int updtaeCartItem(String barcode, int amount) {

		int rowsAcffected = -1;
		SQLiteDatabase db = null;
		int itemId = DbManager.getDbManager().getItemsManager()
				.getItemId(barcode);
		int currentAmount = getItmCount(barcode);

		try {

			String where = COL_ITEM_ID + "=" + itemId;

			values.clear();
			values.put(COL_AMOUNT, amount + currentAmount);
			db = dbHelper.getWritableDatabase();

			rowsAcffected = (int) db.update(TABLE_NAME, values, where, null);

		} catch (Exception e) {
			String msg = e.getMessage();
		} finally {
			Utils.closeDb(db);
		}

		return rowsAcffected;

	}

	public boolean itemExists(String barcode) {

		boolean itemExists = false;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		int itemId = DbManager.getDbManager().getItemsManager()
				.getItemId(barcode);

		try {

			String where = COL_ITEM_ID + " = " + itemId;

			db = dbHelper.getReadableDatabase();

			cursor = db.query(TABLE_NAME, null, where, null, null, null, null);

			itemExists = cursor.moveToFirst();

		} catch (Exception e) {
			String msg = e.getMessage();
		} finally {
			Utils.closeDb(db);
			Utils.closeCursor(cursor);

		}

		return itemExists;

	}

//	public int decreaseItemsAmount(String barcode, int amount) {
//
//		int rowsAffected = 0;
//		SQLiteDatabase db = null;
//		Cursor cursor = null;
//
//		try {
//
//
//		} catch (Exception e)
//
//		{
//
//			String msg = e.getMessage();
//		} finally {
//
//		}
//		return rowsAffected;
//
//	}

}
