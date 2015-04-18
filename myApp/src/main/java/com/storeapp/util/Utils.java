package com.storeapp.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.storeapp.AppContextProvider;

import java.util.Calendar;
import java.util.Date;

public class Utils {



    public static  String getString(int resId){
        return AppContextProvider.getContext().getResources().getString(resId);
    }

    public static Date getCurrentDate(){

        Calendar cal = Calendar.getInstance();
        return new Date(cal.getTimeInMillis());
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

	/*public static Date getReadableDate(long dateMs) {

		Date date = new Date(dateMs);
		return date;
	}
*/
    public  static boolean isEmailFormatValid(CharSequence email) {
        if (isNullOrEmpty(email.toString())) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

}
