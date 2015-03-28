package com.storeapp.util;

import android.database.Cursor;
import android.provider.ContactsContract;

import com.storeapp.Activities.AppContextProvider;

/**
 * Created by Amjad on 23-03-2015.
 */
public class ReadPhoneData {

    public void readPhoneOwnerName() {
        Cursor cursor = AppContextProvider.getContext().getContentResolver().query(
                ContactsContract.Profile.CONTENT_URI, null, null, null, null);
        cursor.moveToFirst();

        String phoneOwnerName = cursor.getString(cursor
                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        String phoneNumber = cursor.getString(cursor
                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        String ownerEmail = cursor.getString(cursor
                .getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));

        if (phoneOwnerName != null) {

            String[] OwnerName = phoneOwnerName.split(" ");
            Utils.showToastLong("Email: "+ownerEmail +"       PhoneNumber  "+phoneNumber+"            PhoneOwnerNanem"+phoneOwnerName);

        }

        cursor.close();

    }


}
