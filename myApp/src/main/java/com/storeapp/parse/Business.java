package com.storeapp.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.storeapp.model.BusinessModel;

@ParseClassName("Business")
public class Business extends ParseObject  {

    public static String BUSINESS_CVR = "cvr";
    public static String BUSINESS_NAME = "name";
    public static String BUSINESS_PHONE_NUMBER = "phoneNumber";
    public static String BUSINESS_EMAIL = "email";

    public String getCvr() {
        return getString(BUSINESS_CVR);
    }

    public void setCvr(String cvr) { put(BUSINESS_CVR, cvr); }


    public String getName() {
		return getString(BUSINESS_NAME);
	}

	public void setName(String name) { put(BUSINESS_NAME, name); }


    public String getPhoneNumber() {
        return getString(BUSINESS_PHONE_NUMBER);
    }

    public void setPhoneNumb(String phoneNumber) {
        put(BUSINESS_PHONE_NUMBER, phoneNumber);
    }



    public String getEmail() {
        return getString(BUSINESS_EMAIL);
    }

    public void setEmail(String email) {
        put(BUSINESS_EMAIL, email);
    }




    public  static ParseQuery<Business> getQuery(String cvr){

        ParseQuery<Business> query = ParseQuery.getQuery(Business.class);
        query.whereEqualTo(BUSINESS_CVR, cvr);
        return  query;


    }


    public static Business fromModelData(BusinessModel model) {

    Business business = new Business();

    business.setEmail(model.getEmail());
    business.setCvr(model.getCvr());
    business.setName(model.getName());
    business.setPhoneNumb(model.getPhoneNumber());

    return  business;
}

}
