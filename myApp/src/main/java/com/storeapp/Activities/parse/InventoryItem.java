package com.storeapp.Activities.parse;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Amjad on 19-03-2015.
 */
@ParseClassName("InventoryItem")
public class InventoryItem extends ParseObject {


    public void setBarcode(String barcode) {

        put("barcode", barcode);
    }

    public String getBarcode()
    {
        return getString("barcode");
    }

    public void setEanType(String eanType) {

        put("eanType", eanType);
    }

    public String getEanType()
    {
        return getString("eanType");
    }


    public void setDescription(String description) {

        put("description", description);
    }

    public String getDescription()
    {
        return getString("description");
    }


    public void setAmount(int amount ) {

        put("amount", amount);
    }

    public int getAmount()
    {
        return getInt("amount");
    }


    public void setPurchasePrice(double purchasePrice ) {

        put("purchasePrice", purchasePrice);
    }

    public double getPurchasePrice()
    {
        return getDouble("purchasePrice");
    }

    public void setSellPrice(double sellPrice ) {

        put("sellPrice", sellPrice);
    }

    public double getSellPrice()
    {
        return getDouble("sellPrice");

    }

    public void setCvrNumber(String cvrNumber) {
        put("cvr", cvrNumber);
    }

    public String getCvrNumber() {
        return getString("cvr");
    }


    public void setItemImg(ParseFile itemImage) {
        put("itemImage", itemImage);
    }

    public ParseFile getItemImg() {
        return getParseFile("itemImage");
    }

    public static ParseQuery<InventoryItem> getQuery(){

        return ParseQuery.getQuery(InventoryItem.class);

    }
}
