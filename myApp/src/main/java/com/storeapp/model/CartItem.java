package com.storeapp.model;

/**
 * Created by Amjad on 02-04-2015.
 */
public class CartItem {

    private int id;
    private String parseIventoryItemId;
    private String barcode;
    private String description;
    private int amount;
    private double sellPrice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParseIventoryItemId(){
        return parseIventoryItemId;
    }

    public void  setParseIventoryItemId(String parseIventoryItemId){
        this.parseIventoryItemId = parseIventoryItemId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }
}
