package com.gisfy.ntfp.Collectors;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.gisfy.ntfp.Login.Models.CollectorUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.Utils.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class PriceListPage extends AppCompatActivity {
    private SharedPref pref;
private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_list_page);
        pref = new SharedPref(this);
        tableLayout = findViewById(R.id.tableLayout);
        new priceListFeach().execute();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    JSONArray jsonArray;
    private class priceListFeach extends AsyncTask<Void, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);

        }
        @Override
        protected String doInBackground(Void... params) {
            CollectorUser user = new SharedPref(PriceListPage.this).getCollector();
            try {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                // Replace with your API URL


                JSONObject jsonBody = new JSONObject();
                jsonBody.put("division", user.getDivision());

                // Convert the JSON object to a string
                String requestBody = jsonBody.toString();

                RequestBody requestJsonBody = RequestBody.create(MediaType.parse("application/json"), requestBody);
                // Replace with your request body, if needed

                Request request = new Request.Builder()
                        .url("https://vanasree.com/NTFPAPI/API/NTFPpricingList")
                        .method("POST", requestJsonBody)
                        .addHeader("Content-Type", "application/json")
                        .build();



                okhttp3.Response response = client.newCall(request).execute();
                String responseData = response.body().string();


                if (response.isSuccessful()) {
                    ResponseBody responseBodyy = response.body();

                    if(responseBodyy != null){
                        try {

                            jsonArray = new JSONArray(responseData);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            findViewById(R.id.spin_kit).setVisibility(View.GONE);
                        }
                    }

                } else {

                    // Handle non-successful response
                    return "Error: " + response.code();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            try {

                TableRow headerRow = new TableRow(PriceListPage.this);


                TextView ntfpHeader = createHeaderTextView("NTFP");
                TextView grade1Header = createHeaderTextView("Grade 1");
                TextView grade2Header = createHeaderTextView("Grade 2");
                TextView grade3Header = createHeaderTextView("Grade 3");

                headerRow.addView(ntfpHeader);
                headerRow.addView(grade1Header);
                headerRow.addView(grade2Header);
                headerRow.addView(grade3Header);

                tableLayout.addView(headerRow);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);


                    String ntfp = jsonObject.getString("ntfp");
                    double grade1 = jsonObject.getDouble("grade1");
                    double grade2 = jsonObject.getDouble("grade2");
                    double grade3 = jsonObject.getDouble("grade3");

                    TableRow tableRow = new TableRow(PriceListPage.this);

                    TextView ntfpTextView = createDataTextView(ntfp);
                    TextView grade1TextView = createDataTextView(String.valueOf(grade1));
                    TextView grade2TextView = createDataTextView(String.valueOf(grade2));
                    TextView grade3TextView = createDataTextView(String.valueOf(grade3));

                    tableRow.addView(ntfpTextView);
                    tableRow.addView(grade1TextView);
                    tableRow.addView(grade2TextView);
                    tableRow.addView(grade3TextView);

                    tableLayout.addView(tableRow);
                    findViewById(R.id.spin_kit).setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }

        }
        }
    private TextView createHeaderTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundColor(Color.GREEN);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18); // Increase text size
        textView.setTypeface(null, Typeface.BOLD); // Make text bold
        textView.setPadding(8, 8, 8, 8); // Add padding
        return textView;
    }

      private TextView createDataTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(8, 8, 8, 8); // Add padding
        return textView;
      }
    }