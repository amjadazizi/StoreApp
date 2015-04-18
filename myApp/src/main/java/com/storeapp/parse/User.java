package com.storeapp.parse;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

/**
 * Created by Amjad on 16-03-2015.
 */
@ParseClassName("_User")
public class User extends ParseUser {

    public static final String COL_FIRST_NAME = "firstName";
    public static final String COL_LAST_NAME = "lastName";
    public static final String COL_BUSINESS_CVR = "businessCvr";
    public static final String COL_PHONE_NUMBER = "phoneNumber";
    public static final String COL_PROFILE_IMAGE = "profileImage";


    public String getFirstName() {
        return getString(COL_FIRST_NAME);
    }

    public void setFirstName(String firstName) {
        put(COL_FIRST_NAME, firstName);
    }


    public String getLastName() {
        return getString(COL_LAST_NAME);
    }

    public void setLastName(String lastName) {
        put(COL_LAST_NAME, lastName);
    }


    public String getBusinessCvr() {
        return getString(COL_BUSINESS_CVR);
    }

    public void setBusinessCvr(String businessCvr) {
        put(COL_BUSINESS_CVR, businessCvr);
    }


    public String getPhoneNumber() {
        return getString(COL_PHONE_NUMBER);
    }

    public void setPhoneNumber(String phoneNumber) {
        put(COL_PHONE_NUMBER, phoneNumber);
    }

    public void setProfileImage(ParseFile image){
           put(COL_PROFILE_IMAGE, image);
    }


    public ParseFile getProfileImage(){
        return  getParseFile(COL_PROFILE_IMAGE);
    }

}
