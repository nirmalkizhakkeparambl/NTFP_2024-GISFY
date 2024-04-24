package com.gisfy.ntfp.Utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FCMNotifications {

    final static String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final static String serverKey = "key=" + "AAAAZP13dhI:APA91bESFI0QeDyIJh7TnvUD_Jnb0yxKOHsBOXokp8eXb6tdi-pwd0t7CcaYyUwRn6rsD2UJDCWtuXocLGSf-8o8ac0y4rKQKlje_gru78dlRCsJp7d6yJVMT6yDkpdvF3I9E3WizczW";
    final static String contentType = "application/json";
    final static String TAG = "NOTIFICATION TAG";

    public static boolean NotifyUser(Context context, String TOPIC, String NOTIFICATION_TITLE, String NOTIFICATION_MESSAGE){
        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);

            notification.put("to", "/topics/"+TOPIC);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Log.e("FB Notification", "onCreate: " + e.getMessage() );
        }
        sendNotification(context,notification);
        return true;
    }

    public static boolean sendNotification(Context context, JSONObject notification) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        return true;
    }
}
