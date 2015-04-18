package com.storeapp.model;

/**
 * Created by Amjad on 12-04-2015.
 */
public class TodaysTurnoverModel {

    private  double cash;
    private double creditCard;
    private int dayOfMonth;
    private double totalSale;

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public double getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(double creditCard) {
        this.creditCard = creditCard;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

/*
    public double getTotalSale() {
        return totalSale;
    }
*/

    public void setTotalSale(double totalSale) {
        this.totalSale = totalSale;
    }
}
