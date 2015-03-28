package com.storeapp.Activities.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Amjad on 14-03-2015.
 */
@ParseClassName("NewUser")
public class NewUser extends ParseObject {

    public String getCvr() {
        return getString("cvr");
    }

    public void setCvr(String cvr) { put("cvr", cvr); }

    public String getEmail() {
        return getString("email");
    }

    public void setEmail(String email) { put("email", email); }

    public static ParseQuery<NewUser> getQuery() {
        return ParseQuery.getQuery(NewUser.class);
    }

}
