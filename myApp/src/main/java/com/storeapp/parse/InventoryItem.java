package com.storeapp.parse;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Amjad on 19-03-2015.
 */
@ParseClassName("InventoryItem")
public class InventoryItem extends ParseObject {

    public static final String COL_BARCODE = "barcode";
    public static final String COL_EAN_TYPE = "eanType";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_AMOUNT = "amount";
    public static final String COL_PURCHASE_PRICE = "purchasePrice";
    public static final String COL_SELL_PRICE = "sellPrice";
    public static final String COL_CVR = "cvr";
    public static final String COL_ITEM_IMAGE = "itemImage";

    public void setBarcode(String barcode) {

        put(COL_BARCODE, barcode);
    }

    public String getBarcode()
    {
        return getString(COL_BARCODE);
    }

    public void setEanType(String eanType) {

        put(COL_EAN_TYPE, eanType);
    }

    public String getEanType()
    {
        return getString(COL_EAN_TYPE);
    }

    public void setDescription(String description) {

        put(COL_DESCRIPTION, description);
    }

    public String getDescription()
    {
        return getString(COL_DESCRIPTION);
    }

    public void setAmount(int amount ) {

        put(COL_AMOUNT, amount);
    }

    public int getAmount()
    {
        return getInt(COL_AMOUNT);
    }

    public void setPurchasePrice(double purchasePrice ) {

        put(COL_PURCHASE_PRICE, purchasePrice);
    }

    public double getPurchasePrice()
    {
        return getDouble(COL_PURCHASE_PRICE);
    }

    public void setSellPrice(double sellPrice ) {

        put(COL_SELL_PRICE, sellPrice);
    }

    public double getSellPrice()
    {
        return getDouble(COL_SELL_PRICE);
    }

    public void setCvrNumber(String cvrNumber) {
        put(COL_CVR, cvrNumber);
    }

    public String getCvrNumber() {
        return getString(COL_CVR);
    }


    public void setItemImg(ParseFile itemImage) {
        put(COL_ITEM_IMAGE, itemImage);
    }

    public ParseFile getItemImg() {
        return getParseFile(COL_ITEM_IMAGE);
    }

    public static ParseQuery<InventoryItem> getQuery(String cvr){

        ParseQuery<InventoryItem> query = ParseQuery.getQuery(InventoryItem.class);
        query.whereEqualTo(COL_CVR,cvr);
        return query;

    }
}
