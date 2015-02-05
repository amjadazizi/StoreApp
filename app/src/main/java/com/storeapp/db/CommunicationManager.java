package com.storeapp.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.storeapp.model.Message;
import com.storeapp.util.Utils;

public class CommunicationManager {

	public static final String TABLE_NAME = "messages";
	public static final String COL_ID = BaseColumns._ID;
	public static final String COL_HEADLINE = "headline";
	public static final String COL_MESSAGE = "message";
	public static final String COL_TIME_STAMP = "timeStamp";
	public static final String COL_SENDER = "sender";

	private SQLiteOpenHelper dbHelper;
	private ContentValues values;

	public CommunicationManager(SQLiteOpenHelper dbHelper) {

		this.dbHelper = dbHelper;
		this.values = new ContentValues();
	}

	public void onCreate(SQLiteDatabase db) {

		try {
			String sql = String
					.format("create table %s (%s integer primary key autoincrement, %s text not null, "
							+ "%s text not null, %s integer not null,%s text not null)",

					TABLE_NAME, COL_ID, COL_HEADLINE, COL_MESSAGE,
							COL_TIME_STAMP, COL_SENDER);

			db.execSQL(sql);

		} catch (Exception e) {
			String a = e.getMessage();
			String aa = a;
		}
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public int saveMsg(Message message) {

		int msgId = -1;
		SQLiteDatabase db = null;

		try {

			values.clear();
			values.put(COL_HEADLINE, message.getHeadline());
			values.put(COL_MESSAGE, message.getMessage());
			values.put(COL_SENDER, message.getSender());
			values.put(COL_TIME_STAMP, message.getTime());

			db = dbHelper.getWritableDatabase();
			msgId = (int) db.insert(TABLE_NAME, null, values);

		} catch (Exception e) {
			String a = e.getMessage();

		} finally {
			Utils.closeDb(db);

		}
		return msgId;
	}

	public Message getMessage(int messageId) {

		Message message = null;
		Cursor cursor = null;
		SQLiteDatabase db = null;

		try {

			String selection = COL_ID + "=" + messageId;
			db = dbHelper.getReadableDatabase();
			cursor = db.query(TABLE_NAME, null, selection, null, null, null,
					null);
			if (cursor.moveToFirst()) {
				message = new Message();

				String sender = cursor.getString(cursor
						.getColumnIndex(COL_SENDER));
				message.setSender(sender);

				String headline = cursor.getString(cursor
						.getColumnIndex(COL_HEADLINE));
				message.setHeadline(headline);

				String messagetxt = cursor.getString(cursor
						.getColumnIndex(COL_MESSAGE));
				message.setMessage(messagetxt);

				long sentTime = cursor.getLong(cursor
						.getColumnIndex(COL_TIME_STAMP));
				message.setTime(sentTime);
			}

		} catch (Exception e) {
			String msg = e.getMessage();
		} finally {
			Utils.closeCursor(cursor);
			Utils.closeDb(db);

		}
		return message;

	}

	public Cursor getMessagesByTime(long timeMS) {

		Cursor cursor = null;
		SQLiteDatabase db = null;

		try {

			String where = null;

			if (timeMS > 0) {
				long requiredTime = System.currentTimeMillis() - timeMS;
				where = COL_TIME_STAMP + " > " + requiredTime;

			}

			db = dbHelper.getReadableDatabase();

			cursor = db.query(TABLE_NAME, null, where, null, null, null,
					COL_TIME_STAMP + " DESC");

		} catch (Exception e) {
			String msg = e.getMessage();

		} finally {

		}
		return cursor;

	}

	public int deleteMessages(int olderThanDays) {

		int rowsAffected = 0;
		SQLiteDatabase db = null;

		try {

			if (olderThanDays > 0) {
				long timeMS = olderThanDays * 24 * 60 * 60 * 1000;
				long requiredTime = System.currentTimeMillis() - timeMS;
				String where = COL_TIME_STAMP + " < " + requiredTime;
				db = dbHelper.getWritableDatabase();
				rowsAffected = db.delete(TABLE_NAME, where, null);
			}

		} catch (Exception e) {
			String msg = e.getMessage();
		} finally {
			Utils.closeDb(db);
		}

		return rowsAffected;

	}
}
