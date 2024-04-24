package com.gisfy.ntfp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.gisfy.ntfp.Login.Models.CollectorUser;
import com.gisfy.ntfp.Login.Models.RFOUser;
import com.gisfy.ntfp.Login.Models.VSSUser;
import com.google.gson.Gson;

public class SharedPref {
    private SharedPreferences sharedpreferences;
    public static String MyPREFERENCES="Userspref";
    SharedPreferences.Editor editor;
    private Context context;
    public SharedPref(Context context){
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        this.context=context;
    }

    public  void saveVSS(VSSUser model){
        editor = sharedpreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(model);
        editor.putString("VSSUser", json);
        editor.apply();
    }
    public VSSUser getVSS(){
        Gson gson = new Gson();
        String json = sharedpreferences.getString("VSSUser", "");
        return gson.fromJson(json, VSSUser.class);
    }
    public  void saveRFO(RFOUser model){
        editor = sharedpreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(model);
        editor.putString("RFOUser", json);
        editor.apply();
    }
    public RFOUser getRFO(){
        Gson gson = new Gson();
        String json = sharedpreferences.getString("RFOUser", "");
        return gson.fromJson(json, RFOUser.class);
    }
    public void setLanguage(String language){
        editor = sharedpreferences.edit();
        editor.putString("language", language);
        editor.apply();
    }
    public String getLanguage(){
        if (sharedpreferences.getString("language", "NA").equals(Constants.MALAYALAM))
            return Constants.MALAYALAM;
        else
            return Constants.ENGLISH;
    }
    public  void saveCollector(CollectorUser model){
        editor = sharedpreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(model);
        editor.putString("CollectorUser", json);
        editor.apply();
    }
    public CollectorUser getCollector(){
        Gson gson = new Gson();
        String json = sharedpreferences.getString("CollectorUser", "");
        return gson.fromJson(json, CollectorUser.class);
    }
    public void addString(String key,String value){
        editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public void addBool(String key,boolean value){
        editor = sharedpreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public void addInt(String key,int value){
        editor = sharedpreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }
    public String getString(String key){
        return sharedpreferences.getString(key, "NA");
    }
    public int getInt(String key){
        return sharedpreferences.getInt(key, 0);
    }
    public boolean getBool(String key){
        return sharedpreferences.getBoolean(key, false);
    }
    public void clearPref(){
        SharedPreferences preferences =context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
