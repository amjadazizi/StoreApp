package com.storeapp.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("SoldItem")
public class SoldItem extends ParseObject {

    public static final String COL_INVENTORY_ITEM  = "inventoryItem";
    public static final String COL_AMOUNT  = "amount";

    public void setInventoryItem(InventoryItem inventoryItem) {

        put(COL_INVENTORY_ITEM, inventoryItem);
    }

    public InventoryItem getInventoryItem() {

        return (InventoryItem) get(COL_INVENTORY_ITEM);
    }


    public int getAmount() {

        return getInt(COL_AMOUNT);
    }

    public void setAmount(int amount) {
        put(COL_AMOUNT, amount);
    }

    public static ParseQuery<SoldItem> getQuery(String cvr) {

        ParseQuery<InventoryItem> iiQuery = InventoryItem.getQuery(cvr);
        ParseQuery<SoldItem> query= ParseQuery.getQuery(SoldItem.class);
        query.include(COL_INVENTORY_ITEM);
        query.whereMatchesQuery (COL_INVENTORY_ITEM, iiQuery);
        return query;
    }



}
