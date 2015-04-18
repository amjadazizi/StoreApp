package com.storeapp.parse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Amjad on 14-04-2015.
 */
public class ParseDateComparing {
    private Date fromDate;
    private Date toDate;

    public  ParseDateComparing(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        //Set GMT as the timezone (DK)
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        Calendar cal = Calendar.getInstance();
        int todayDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int todayMonth  = cal.get(Calendar.MONTH)+1;
        int todayYear = cal.get(Calendar.YEAR);



        String from =  String.format("%d-%d-%dT00:00:00Z", todayYear, todayMonth, todayDayOfMonth);
        String to =  String.format("%d-%d-%dT23:59:59Z", todayYear, todayMonth, todayDayOfMonth);


        try {
            fromDate = dateFormat.parse(from);
            toDate = dateFormat.parse(to);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }
}
