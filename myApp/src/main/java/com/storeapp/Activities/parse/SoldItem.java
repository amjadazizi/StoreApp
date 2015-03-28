package com.storeapp.Activities.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("SoldItem")
public class SoldItem extends ParseObject {


public void setBarcode(String barcode){

    put("barcode", barcode);
}

    public String getBarcode(){

        return getString("barcode");
    }


    public int getAmount() {

        return getInt("amount");
    }

    public void setAmount(int amount) {
        put("amount", amount);
    }


    public void setCvr(String cvr){

        put("cvr", cvr);
    }

    public String getCvr(){

        return getString("cvr");
    }


    public static ParseQuery<SoldItem> getQuery(){

        return  ParseQuery.getQuery(SoldItem.class);
    }


}
