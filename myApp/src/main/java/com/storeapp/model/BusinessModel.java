package com.storeapp.model;

import com.storeapp.util.Utils;

/**
 * Created by Amjad on 12-03-2015.
 */
public class BusinessModel extends  BaseModel {

    private String name;
    private String cvr;
    private String email;
    private String phoneNumber;

    public BusinessModel() {
    }

    public BusinessModel(String name, String cvr, String email, String phoneNumber ){
        this.name = name;
        this.cvr=cvr;
        this.email=email;
        this.phoneNumber=phoneNumber;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCvr() {
        return cvr;
    }

    public void setCvr(String cvr) {
        this.cvr = cvr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isValid(){

        boolean valid = true;

        valid= valid ? (Utils.isNullOrEmpty(name) ? false: valid) : valid;
        valid= valid ? (Utils.isEmailValid(cvr) ? false: valid) : valid;
        valid= valid ? (Utils.isNullOrEmpty(email) ? false: valid) : valid;
        valid= valid ? (Utils.isNullOrEmpty(phoneNumber) ? false: valid) : valid;

        return  valid;

    }


   // @Override
   // public static String getSharedPrefsKey() {
    //    return "businessModelPrefsKey";
    //}
}
