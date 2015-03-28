package com.storeapp.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Patterns;
import android.widget.Toast;

import com.storeapp.Activities.AppContextProvider;
import com.storeapp.sweetalert.SweetAlertDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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


    public static  boolean isEmailValid(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
