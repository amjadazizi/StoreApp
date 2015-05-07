package com.storeapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.storeapp.AppContextProvider;

public class Prefs {
    public static final String KEY_IS_USER_LOGGED_IN = "pref_is_user_logged_in";
    public static final String PREF_TASK_TYPE = "pref_task_type";
    public static final String PREF_BUSINESS_CVR = "pref_business_cvr";
    public static  final String PREF_LOGED_IN_USER ="pref_loged_in_user";
    public static  final String PREF_IS_USER_ADMIN ="pref_is_user_admin";
    public static  final String PREF_TOTAL_RECEIVED_AMOUNT ="pref_total_received_amount";


    public static SharedPreferences getPrefs() {
        Context context = AppContextProvider.getContext();
        return PreferenceManager.getDefaultSharedPreferences(context);
    }


    public static void setTotalReceivedAmount(String totalReceivedAmount){
        SharedPreferences prefs = Prefs.getPrefs();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_TOTAL_RECEIVED_AMOUNT, totalReceivedAmount);
        editor.commit();

    }

    public  static String getTotalReceviedAmount(){


        SharedPreferences prefs = Prefs.getPrefs();
        return  prefs.getString(PREF_TOTAL_RECEIVED_AMOUNT,null);
    }



        public static void setTaskType(int taskType) {

        SharedPreferences prefs = Prefs.getPrefs();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREF_TASK_TYPE, taskType);
        editor.commit();
    }

    public static int getTaskType() {
        SharedPreferences prefs = Prefs.getPrefs();
        return prefs.getInt(PREF_TASK_TYPE, -1);
    }

    public static void setBusinessCvr(String cvr) {
        SharedPreferences prefs = Prefs.getPrefs();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_BUSINESS_CVR, cvr);
        editor.commit();
    }

    public static String getBusinessCvr() {
        SharedPreferences prefs = Prefs.getPrefs();
        return prefs.getString(PREF_BUSINESS_CVR, null);
    }


 public static void setLogedInUser(String email){

        SharedPreferences prefs = Prefs.getPrefs();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_LOGED_IN_USER, email);
        editor.commit();
    }

    public  static  String getPrefLogedInUser(){
        SharedPreferences prefs = Prefs.getPrefs();
        return prefs.getString(PREF_LOGED_IN_USER,null);
    }

    public static boolean isUserLoggedIn() {

        SharedPreferences prefs = getPrefs();
        return prefs.getBoolean(KEY_IS_USER_LOGGED_IN, false);
    }

   public static void setUserLoggedIn(boolean loggedIn) {

        SharedPreferences prefs = getPrefs();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_IS_USER_LOGGED_IN, loggedIn);
        editor.commit();
    }

    public static boolean isUserAdmin(){
        SharedPreferences prefs = getPrefs();
        return prefs.getBoolean(PREF_IS_USER_ADMIN,false);

    }

    public static void setUserAsAdmin(boolean isUserAdmin){
        SharedPreferences prefs = getPrefs();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREF_IS_USER_ADMIN, isUserAdmin);
        editor.commit();

    }


}
