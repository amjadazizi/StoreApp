package com.storeapp.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Amjad on 17-04-2015.
 */
@ParseClassName("Admin")

public class Admin extends ParseObject{

    public static final String COL_ADMIN_EAMIL = "email";

    public  String getAdminEamil() {
        return getString(COL_ADMIN_EAMIL);
    }

    public void setAdminEmail(String email){

        put(COL_ADMIN_EAMIL,email);
    }

    public static ParseQuery<Admin> getQuery(String email){

        ParseQuery<Admin> adminParseQuery = ParseQuery.getQuery(Admin.class);
        adminParseQuery.whereEqualTo(COL_ADMIN_EAMIL, email);
        return adminParseQuery;

    }

}
