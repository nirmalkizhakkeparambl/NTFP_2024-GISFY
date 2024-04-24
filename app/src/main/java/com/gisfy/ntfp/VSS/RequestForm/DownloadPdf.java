package com.gisfy.ntfp.VSS.RequestForm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gisfy.ntfp.R;

public class DownloadPdf extends AppCompatActivity {
    WebView web;
String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_pdf);
        web = (WebView) findViewById(R.id.webView1);
        web = new WebView(this);
        if (getIntent().hasExtra("IdTrans")){
            id= getIntent().getStringExtra("IdTrans");
        }
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl("http://122.175.5.221/ForestPlus/VssTransitPass.aspx"+"?TransUniqueId="+id);
        web.setWebViewClient(new myWebClient());
        web.setWebChromeClient(new WebChromeClient());
        setContentView(web);
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);


        }
    }

    //flip screen not loading again
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (web.canGoBack()) {
                        web.goBack();
                    } else {
                        backButtonHandler();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                DownloadPdf.this);


        // Setting Dialog Title
        // Setting Dialog Message

        alertDialog.setTitle("Your App Name");

        // I've included a simple dialog icon in my project named "dialog_icon", which's image file is copied and pasted in all "drawable" folders of "res" folders of the project. You can include any dialog image of your wish and rename it to dialog_icon.

        alertDialog.setMessage("Exit Now?");

        // Setting Icon to Dialog
        // Setting Positive "Yes" Button

        alertDialog.setPositiveButton("Exit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        // Setting Negative "NO" Button

        alertDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });
        // Showing Alert Message
        alertDialog.show();
    }
}