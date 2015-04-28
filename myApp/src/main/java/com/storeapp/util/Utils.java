package com.storeapp.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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


    public  static boolean isEmailFormatValid(CharSequence email) {
        if (isNullOrEmpty(email.toString())) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }


    public static final int[] COLORFUL_COLORS = {
            //////////////COLORFULS
            Color.rgb(192, 255, 140), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140),
            Color.rgb(140, 234, 255), Color.rgb(255, 140, 157),
            //////////////PASTEL
           Color.rgb(64, 89, 128), Color.rgb(149, 165, 124), Color.rgb(217, 184, 162),
            Color.rgb(191, 134, 134), Color.rgb(179, 48, 80),
            //////////////JOYFULL
          //Color.rgb(217, 80, 138), Color.rgb(254, 149, 7), Color.rgb(254, 247, 120),
            //Color.rgb(106, 167, 134), Color.rgb(53, 194, 209)
            ////LibertyColors
             // Color.rgb(207, 248, 246), Color.rgb(148, 212, 212), Color.rgb(136, 180, 187),
           // Color.rgb(118, 174, 175), Color.rgb(42, 109, 130)

    };

}
