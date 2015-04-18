package com.storeapp.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;


/**
 * Created by Amjad on 05-04-2015.
 */
@ParseClassName("TodaysTurnover")
public class TodaysTurnover extends ParseObject{

    private String cvr;
    private Date date;
    private double cash;
    private double creditCard;

    public  static final String TODAYS_TURNOVER_BUSINESS_CVR = "cvr";
    public  static final String TODAYS_TURNOVER_DATE = "date";
    public  static final String TODAYS_TURNOVER_CASH = "cash";
    public  static final String TODAYS_TURNOVER_CREDIR_CARD = "creditCard";



    public String getCvr() {
        return getString(TODAYS_TURNOVER_BUSINESS_CVR);
    }

    public void setCvr(String cvr) {
        put(TODAYS_TURNOVER_BUSINESS_CVR,cvr);
    }

    public Date getDate() {

        return getDate(TODAYS_TURNOVER_DATE);
    }

    public void setDate(Date date) {
            put(TODAYS_TURNOVER_DATE, date);
    }

    public double getCash() {
        return getDouble(TODAYS_TURNOVER_CASH);
    }

    public void setCash(double cash) {
        put(TODAYS_TURNOVER_CASH,cash);
    }

    public double getCreditCard() {
        return getDouble(TODAYS_TURNOVER_CREDIR_CARD);
    }

    public void setCreditCard(double creditCard) {
        put(TODAYS_TURNOVER_CREDIR_CARD, creditCard);
    }

    public  static ParseQuery<TodaysTurnover> getQuery(String cvr){

        ParseQuery<TodaysTurnover> query = ParseQuery.getQuery(TodaysTurnover.class);
        query.whereEqualTo(TODAYS_TURNOVER_BUSINESS_CVR, cvr);
        return  query;


    }

}
