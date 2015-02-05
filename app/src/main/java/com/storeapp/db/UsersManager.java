package com.storeapp.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.text.method.MovementMethod;

import com.storeapp.model.User;
import com.storeapp.util.Utils;

public class UsersManager {

	public static final String TABLE_NAME = "users";
	public static final String COL_ID = BaseColumns._ID;
	public static final String COL_NAME = "name";
	public static final String COL_INITIALS = "initials";
	public static final String COL_PHONE_NUMB = "phoneNumb";
	public static final String COL_EMAIL = "email";
	public static final String COL_PASSWORD = "password";

	private SQLiteOpenHelper dbHelper;

	private ContentValues values;
	ArrayList<String> list = new ArrayList<String>();

	public UsersManager(SQLiteOpenHelper dbHelper) {

		this.dbHelper = dbHelper;
		this.values = new ContentValues();
	}

	public void onCreate(SQLiteDatabase db) {

		try {
			String sql = String
					.format("create table %s (%s integer primary key autoincrement, %s text not null, %s text not null, %s text not null, %s text not null,%s text not null)",

					TABLE_NAME, COL_ID, COL_NAME, COL_INITIALS, COL_EMAIL,
							COL_PASSWORD, COL_PHONE_NUMB);

			db.execSQL(sql);

		} catch (Exception e) {
			String a = e.getMessage();
		}
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public int createUser(User user) {

		int userId = -1;

		SQLiteDatabase db = null;

		try {

			values.clear();
			values.put(COL_NAME, user.getName());
			values.put(COL_INITIALS, user.getInitials());
			values.put(COL_EMAIL, user.getEmail());
			values.put(COL_PASSWORD, user.getPassword());
			values.put(COL_PHONE_NUMB, user.getPhoneNumb());

			db = dbHelper.getWritableDatabase();
			userId = (int) db.insert(TABLE_NAME, null, values);

		} catch (Exception e) {
			String a = e.getMessage();

		} finally {
			Utils.closeDb(db);

		}

		return userId;
	}

	public boolean loginExists(String initials, String password) {

		boolean exists = false;
		Cursor cursor = null;
		SQLiteDatabase db = null;

		try {

			if (!Utils.isNullOrEmpty(initials)
					&& !Utils.isNullOrEmpty(password)) {
				String selection = String.format("%s = ? and %s = ?",
						COL_INITIALS, COL_PASSWORD);
				String[] selectionArgs = { initials, password };

				db = dbHelper.getReadableDatabase();
				cursor = db.query(TABLE_NAME, null, selection, selectionArgs,
						null, null, null);
				exists = cursor.moveToFirst();
			}

		} catch (Exception e) {
			String a = e.getMessage();

		} finally {
			Utils.closeCursor(cursor);
			Utils.closeDb(db);

		}

		return exists;
	}

	public boolean initialsExists(String initials) {

		boolean exists = false;
		Cursor cursor = null;
		SQLiteDatabase db = null;

		try {

			String selection = String.format("%s = ?", COL_INITIALS);
			String[] selectionArgs = { initials };

			db = dbHelper.getReadableDatabase();
			cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null,
					null, null);
			exists = cursor.moveToFirst();

		} catch (Exception e) {
			String a = e.getMessage();

		} finally {
			Utils.closeCursor(cursor);
			Utils.closeDb(db);

		}

		return exists;
	}

	public Cursor getAllEmployees() {

		Cursor cursor = null;
		SQLiteDatabase db = null;

		try {

			db = dbHelper.getReadableDatabase();
			cursor = db.query(TABLE_NAME, null, null, null, null, null,
					COL_NAME);

		} catch (Exception e) {
			String a = e.getMessage();

		} finally {
			// Utils.closeCursor(cursor);
			// Utils.closeDb(db);

		}

		return cursor;
	}

	public User getUser(int userId) {

		User user = null;
		Cursor cursor = null;
		SQLiteDatabase db = null;

		try {

			String selection = COL_ID + " = " + userId;
			db = dbHelper.getReadableDatabase();
			cursor = db.query(TABLE_NAME, null, selection, null, null, null,
					null);
			if (cursor.moveToFirst()) {
				user = new User();

				String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
				user.setName(name);

				String phoneNumber = cursor.getString(cursor
						.getColumnIndex(COL_PHONE_NUMB));
				user.setPhoneNumb(phoneNumber);
				String email = cursor.getString(cursor
						.getColumnIndex(COL_EMAIL));
				user.setEmail(email);

			}

		} catch (Exception e) {
			String a = e.getMessage();

		} finally {
			Utils.closeCursor(cursor);
			Utils.closeDb(db);

		}
		return user;

	}

	public Cursor getAllEmpNames(String searchTerm) {

		String[] cols = { COL_ID, COL_NAME };
		Cursor cursor = null;
		SQLiteDatabase db = null;

		try {

			String where = null;
			if (!Utils.isNullOrEmpty(searchTerm)) {
				where = COL_NAME + " LIKE '" + searchTerm + "%'";
			}

			db = dbHelper.getReadableDatabase();
			cursor = db.query(TABLE_NAME, cols, where, null, null, null,
					COL_NAME);

		} catch (Exception e) {
			String a = e.getMessage();

		} finally {
			// Utils.closeCursor(cursor);
			// Utils.closeDb(db);

		}

		return cursor;
	}
}
