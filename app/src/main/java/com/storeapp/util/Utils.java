package com.storeapp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.storeapp.AppContextProvider;
import com.storeapp.Prefs;

public class Utils {

	public static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	public static String convertTime(long time) {

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return sdf.format(cal.getTime());

	}

	public static void closeDb(SQLiteDatabase db) {
		if (db != null) {
			try {
				db.close();
			} finally {

			}
		}

	}

	public static void closeCursor(Cursor cursor) {
		if (cursor != null) {
			try {
				cursor.close();
			} finally {

			}
		}

	}

	public static boolean isNullOrEmpty(String value) {

		if (value == null) {
			return true;
		}

		if (value.trim().length() == 0) {
			return true;
		}

		return false;

	}

	public static void showToastLong(String message) {
		Toast.makeText(AppContextProvider.getContext(), message,
				Toast.LENGTH_LONG).show();
	}

	public static void showToastShort(String message) {
		Toast.makeText(AppContextProvider.getContext(), message,
				Toast.LENGTH_SHORT).show();
	}

	public static Date getReadableDate(long dateMs) {

		Date date = new Date(dateMs);
		return date;
	}

}
