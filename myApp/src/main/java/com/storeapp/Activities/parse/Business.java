package com.storeapp.Activities.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.storeapp.model.BusinessModel;

@ParseClassName("Business")
public class Business extends ParseObject  {


    public String getCvr() {
        return getString("cvr");
    }

    public void setCvr(String cvr) { put("cvr", cvr); }


    public String getName() {
		return getString("name");
	}

	public void setName(String name) { put("name", name); }


    public String getPhoneNumber() {
        return getString("phoneNumber");
    }

    public void setPhoneNumb(String phoneNumber) {
        put("phoneNumber", phoneNumber);
    }



    public String getEmail() {
        return getString("email");
    }

    public void setEmail(String email) {
        put("email", email);
    }




	public String getPassword() {
		return getString("password");
	}

	public void setPassword(String password) {
		put("password", password);
	}


    public static ParseQuery<Business> getQuery() {
        return ParseQuery.getQuery(Business.class);
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
