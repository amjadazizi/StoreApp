package com.storeapp.model;

public class Item {

	private String description;
	private double purchPrice;

	private double sellPrice;
	private int amount;
	private String barcode;
	private String eanType;
	private String expDate;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPurchPrice() {
		return purchPrice;
	}

	public void setPurchPrice(Double purchPrice) {
		this.purchPrice = purchPrice;
	}

	public double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(Double sellPrice) {
		this.sellPrice = sellPrice;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getEanType() {
		return eanType;
	}

	public void setEanType(String eanType) {
		this.eanType = eanType;
	}

	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

}
