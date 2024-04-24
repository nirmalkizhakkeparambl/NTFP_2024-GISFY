package com.gisfy.ntfp.VSS.Collectors;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.StrictMode;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gisfy.ntfp.ExcelImport.ExcelUtil;
import com.gisfy.ntfp.HomePage.Home;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.SqliteHelper.DBHelper;
import com.gisfy.ntfp.Utils.RealPathUtil;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ImportExcel extends AppCompatActivity {
    public static final String TAG = "ImportExcelTag";
    private Context mContext;
    private final int FILE_SELECTOR_CODE = 10000;
    private final List<Map<Integer, Object>> readExcelList = new ArrayList<>();
    String excelPath="";

    private ImageView retake;
    private TextView uploadtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_excel);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mContext = this;

        initViews();

        findViewById(R.id.idcardCardTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStoragePermissionGranted()) {
                    openFileSelector();
                } else {
                    SnackBarUtils.ErrorSnack(ImportExcel.this,getString(R.string.storagepermission));
                }
            }
        });

        retake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStoragePermissionGranted()) {
                    openFileSelector();
                } else {
                    SnackBarUtils.ErrorSnack(ImportExcel.this,getString(R.string.storagepermission));
                }
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initViews() {
        StaticChecks checks = new StaticChecks(this);
        uploadtv = findViewById(R.id.idcardCardTV);
        retake = findViewById(R.id.idcardCard2);
    }



    public boolean isStoragePermissionGranted() {
        String TAG = "Permisiion";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                return false;
            }
        } else {
            //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    private void openFileSelector() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/*");
        startActivityForResult(intent, FILE_SELECTOR_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_SELECTOR_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (uri == null) return;
            Log.i(TAG, "onActivityResult: " + "filePath：" + uri.getPath());
            uploadtv.setText(getResources().getText(R.string.uploadagain));

            excelPath=uri.getPath();
            DoActualReqst(uri);

        }
    }


    public void DoActualReqst(Uri uri){
        try {
            File file = new File(getRealPathFromURI(uri));
            String fileName = file.getName();
            OkHttpClient client = new OkHttpClient();
            String url = "http://vanasree.com/NTFPAPI/API/ReadExcel";
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("InputFile", file.getName(), RequestBody.create(MediaType.parse("application/vnd.ms-excel"), file))
                    .build();
            Log.d("OKKKRespoveBody", body+ "");
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            Thread thread = new Thread(() -> {
                try  {
                    try {
                        Response response = client.newCall(request).execute();
                        String res = response.body().string();
                        Log.i("OKKKLogResponce",  ""+res);
                        if (res.contains("success")) {
                            runOnUiThread(() -> {
                                Toast.makeText(mContext, "Upload Successfull", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(ImportExcel.this, Home.class);
                                startActivity(i);
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, "Something Wrong", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            thread.start();

        }catch (Exception e){
            String exc = e.getLocalizedMessage();
            Toast.makeText(this,exc, Toast.LENGTH_SHORT).show();


        }
    }


    public String getRealPathFromURI(Uri uri) {
        Uri returnUri = uri;
        Cursor returnCursor = getContentResolver().query(returnUri, null, null, null, null);

        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(getFilesDir(), name);
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }

}
