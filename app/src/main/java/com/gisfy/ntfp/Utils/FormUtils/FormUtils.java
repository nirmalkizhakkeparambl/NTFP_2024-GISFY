package com.gisfy.ntfp.Utils.FormUtils;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public class FormUtils {
    Context context;

    public FormUtils(Context context) {
        this.context = context;
    }

    public static String getValue(JSONObject obj, String value) throws JSONException {
        if(obj.has(value))
            return obj.getString(value);
        else
            return "null";
    }

    public static String getIntValue(JSONObject obj, String value) throws JSONException {
        if(obj.has(value))
            return obj.getString(value);
        else
            return "0";
    }

    public static boolean hasValue(JSONObject obj, String value) throws JSONException {
        if(obj.has(value))
            return true;
        else
            return false;
    }
}
