package com.storeapp.Activities.parse;

import android.content.Context;

import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseObject;

/**
 * Created by Amjad on 08-02-2015.
 */
public class ParseInitializer {

    private static String PARSE_APP_ID = "0IQaUkNDTnYl2jNT1O2lhk9WSaRgSpY1X7lw2xkh";
    private static String PARSE_CLIENT_KEY = "7kGAQDO5tjM3RLEAM8n4bDxj7hywiHpwtPbkjfAg";

    public static void initialize(Context context){

        //Register all Parse subclasses
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(NewUser.class);
        ParseObject.registerSubclass(Business.class);
        ParseObject.registerSubclass(InventoryItem.class);
        ParseObject.registerSubclass(SoldItem.class);


        ParseCrashReporting.enable(context);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(context);

        //Initialize parse (must be the last call)
        Parse.initialize(context, PARSE_APP_ID, PARSE_CLIENT_KEY);
    }

}
