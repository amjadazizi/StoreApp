package com.storeapp.db;

import android.R.integer;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.storeapp.model.Item;
import com.storeapp.util.Utils;

public class ItemsManager {

	public static final String TABLE_NAME = "item";
	public static final String COL_ID = BaseColumns._ID;
	public static final String COL_DESCRIPTION = "description";
	public static final String COL_PURCHPRICE = "purchPrice";
	public static final String COL_SELLPRICE = "sellPrice";
	public static final String COL_EXP_DATE = "expDate";
	public static final String COL_BARCODE = "barcode";
	public static final String COL_EANTYPE = "eantype";
	public static final String COL_AMOUNT = "amount";

	private SQLiteOpenHelper dbHelper;
	private ContentValues values;

	public ItemsManager(SQLiteOpenHelper dbHelper) {

		this.dbHelper = dbHelper;
		this.values = new ContentValues();
	}

	public void onCreate(SQLiteDatabase db) {

		try {

			String sql = String
					.format("create table %s(%s integer primary key autoincrement, %s text not null,%s text not null,%s text not null,%s text not null,%s text not null,%s text not null,%s integer not null)",
							TABLE_NAME, COL_ID, COL_DESCRIPTION,
							COL_PURCHPRICE, COL_SELLPRICE, COL_BARCODE,
							COL_EANTYPE, COL_EXP_DATE, COL_AMOUNT);

			db.execSQL(sql);

		} catch (Exception e) {
			String a = e.getMessage();
			String aa = a;
		}
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public int storeItem(Item item) {

		int itemId = -1;
		SQLiteDatabase db = null;

		try {

			values.clear();
			values.put(COL_DESCRIPTION, item.getDescription());
			values.put(COL_PURCHPRICE, item.getPurchPrice());
			values.put(COL_SELLPRICE, item.getSellPrice());
			values.put(COL_BARCODE, item.getBarcode());
			values.put(COL_EXP_DATE, item.getExpDate());
			values.put(COL_EANTYPE, item.getEanType());
			values.put(COL_AMOUNT, item.getAmount());

			db = dbHelper.getWritableDatabase();
			itemId = (int) db.insert(TABLE_NAME, null, values);

		} catch (Exception e) {
			String a = e.getMessage();
			String aa = a;

		} finally {
			Utils.closeDb(db);

		}

		return itemId;
	}

	public boolean itemExists(String barcode) {

		boolean itemExists = false;

		Cursor cursor = null;
		SQLiteDatabase db = null;

		try {

			String selection = String.format("%s = ? ", COL_BARCODE);
			String[] selectionArgs = { barcode };

			db = dbHelper.getReadableDatabase();
			cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null,
					null, null);
			itemExists = cursor.moveToFirst();

		} catch (Exception e) {
			String a = e.getMessage();

		} finally {
			Utils.closeCursor(cursor);
			Utils.closeDb(db);
		}

		return itemExists;
	}

	public int getItmCount(String barcode) {
		int itmCount = 0;
		Cursor cursor = null;
		SQLiteDatabase db = null;

		try {

			String where = COL_BARCODE + " = ?";
			String[] whereargs = { barcode };

			db = dbHelper.getReadableDatabase();
			String[] columns = { COL_AMOUNT };
			cursor = db.query(TABLE_NAME, columns, where, whereargs, null,
					null, null);
			if (cursor.moveToNext()) {
				int colmIndex = cursor.getColumnIndex(COL_AMOUNT);
				itmCount = cursor.getInt(colmIndex);
			}

		} catch (Exception e) {

		} finally {
			Utils.closeCursor(cursor);
			Utils.closeDb(db);

		}

		return itmCount;
	}

	public int increaseItmAmount(String barcode, int amount) {

		int rowsAffected = 0;
		SQLiteDatabase db = null;

		int currentAmount = getItmCount(barcode);

		try {

			String where = COL_BARCODE + " = ?";
			String[] whereagrs = { barcode };

			values.clear();
			values.put(COL_AMOUNT, currentAmount + amount);

			db = dbHelper.getWritableDatabase();

			rowsAffected = db.update(TABLE_NAME, values, where, whereagrs);

		} catch (Exception e) {
			String a = e.getMessage();
			String aa = a;

		} finally {
			Utils.closeDb(db);

		}

		return rowsAffected;
	}

	public int decreaseItemAmount(String barcode, int amount) {

		int rowsAffected = 0;
		SQLiteDatabase db = null;

		int currentAmount = getItmCount(barcode);

		try {

			String where = COL_BARCODE + " = ?";
			String[] whereagrs = { barcode };

			values.clear();
			values.put(COL_AMOUNT, currentAmount - amount);

			db = dbHelper.getWritableDatabase();

			rowsAffected = db.update(TABLE_NAME, values, where, whereagrs);

		} catch (Exception e) {
			String a = e.getMessage();
			String aa = a;

		} finally {
			Utils.closeDb(db);

		}

		return rowsAffected;
	}

	public int getItemId(String barcode) {

		int itemId = 0;

		Cursor cursor = null;
		SQLiteDatabase db = null;

		try {

			String where = COL_BARCODE + " = ?";
			String[] whereagrs = { barcode };

			db = dbHelper.getReadableDatabase();
			String[] columns = { COL_ID };
			cursor = db.query(TABLE_NAME, columns, where, whereagrs, null,
					null, null);

			if (cursor.moveToFirst()) {

				itemId = cursor.getInt(cursor.getColumnIndex(COL_ID));
			}

		} catch (Exception e) {
			String msg = e.getMessage();

		} finally {
			Utils.closeCursor(cursor);
			Utils.closeDb(db);

		}
		return itemId;

	}

	public Cursor getAllItemsInStock() {

		Cursor cursor = null;
		SQLiteDatabase db = null;

		try {

			String[] columns = { COL_ID, COL_AMOUNT, COL_DESCRIPTION,
					COL_PURCHPRICE, COL_SELLPRICE };

			String selection = COL_AMOUNT + ">" + 0;

			db = dbHelper.getReadableDatabase();

			cursor = db.query(TABLE_NAME, columns, selection, null, null, null,
					COL_DESCRIPTION);

		} catch (Exception e) {
			String msg = e.getMessage();
		} finally {

		}

		return cursor;

	}

	public Cursor getSoldItems() {
		Cursor cursor = null;
		SQLiteDatabase db = null;

		try {

			db = dbHelper.getReadableDatabase();

			String selection = COL_AMOUNT + " = " + 0;

			cursor = db.query(TABLE_NAME, null, selection, null, null, null,
					null);

		} catch (Exception e) {

		} finally {
			// Utils.closeCursor(cursor);
			// Utils.closeDb(db);

		}

		return cursor;
	}
}
