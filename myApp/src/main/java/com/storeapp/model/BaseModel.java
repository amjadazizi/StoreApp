package com.storeapp.model;


import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.storeapp.util.Prefs;

/**
 * Created by Amjad on 12-03-2015.
 */
public abstract class BaseModel {

    public void saveInSharedPrefs(){
       SharedPreferences prefs = Prefs.getPrefs();
        SharedPreferences.Editor prefsEditor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this);
        prefsEditor.putString(this.getClass().getSimpleName(), json);
        prefsEditor.commit();
    }

    public static <T extends BaseModel> T getFromSharedPrefs(Class<T> modelClass){
        SharedPreferences prefs = Prefs.getPrefs();
        Gson gson = new Gson();
        String json = prefs.getString(modelClass.getSimpleName(), "");
        T model = gson.fromJson(json, modelClass);
        if(model==null){
            try {
                model = modelClass.newInstance();
            } catch (Exception e) {
            }

        }
        return model;
    }

}
