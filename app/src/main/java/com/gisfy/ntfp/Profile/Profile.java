package com.gisfy.ntfp.Profile;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gisfy.ntfp.HomePage.Home;
import com.gisfy.ntfp.Login.Language;
import com.gisfy.ntfp.Login.LoginActivity;
import com.gisfy.ntfp.Login.Models.CollectorUser;
import com.gisfy.ntfp.Login.Models.RFOUser;
import com.gisfy.ntfp.Login.Models.VSSUser;
import com.gisfy.ntfp.Login.ResetPassword;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.Utils.Constants;
import com.gisfy.ntfp.Utils.ImagePickerActivity;
import com.gisfy.ntfp.Utils.RealPathUtil;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Profile extends AppCompatActivity  {

    private RecyclerView recyclerView;
    private StaticChecks checks;
    private SharedPref pref;
    private ImageView profileImage;
    private final List<ProfileModel> list=new ArrayList<>();
    private String id=null;
    private String type=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        intiViews();
        if (pref.getString("language").equals(Constants.ENGLISH)){
            profileImage.setImageResource(R.drawable.vanashree_logo_english);
        }else {
            profileImage.setImageResource(R.drawable.vanashreelogo);
        }
        switch (type) {
            case Constants.VSS:
                VSSUser vssUser = pref.getVSS();
                id=String.valueOf(vssUser.getVid());
                list.add(new ProfileModel(getString(R.string.division), vssUser.getDivisionname(), R.drawable.vector_email));
                list.add(new ProfileModel(getString(R.string.village), vssUser.getVillage(), R.drawable.vector_email));
                list.add(new ProfileModel(getString(R.string.phoneno), vssUser.getHeadPhoneNumber(), R.drawable.vector_email));
                list.add(new ProfileModel(getString(R.string.range), vssUser.getRangeName(), R.drawable.vector_email));
                list.add(new ProfileModel(getString(R.string.vsshead), vssUser.getVssHead(), R.drawable.vector_email));
                list.add(new ProfileModel("VSS Name", vssUser.getvSSName(), R.drawable.vector_email));
                break;
            case Constants.COLLECTORS:
                CollectorUser user = pref.getCollector();
                id=String.valueOf(user.getCid());
                type="Collector";
                list.add(new ProfileModel(getString(R.string.division), user.getDivision(), R.drawable.vector_email));
                list.add(new ProfileModel(getString(R.string.range), user.getRange(), R.drawable.vector_email));
                list.add(new ProfileModel(getString(R.string.vssrole), user.getvSSName(), R.drawable.vector_email));
                list.add(new ProfileModel(getString(R.string.spoucenamep), user.getSpouseName(), R.drawable.vector_email));
                list.add(new ProfileModel(getString(R.string.collectornamep), user.getCollectorName(), R.drawable.vector_email));
                break;
        }
        ProfileAdapter adapter=new ProfileAdapter( Profile.this,list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        recyclerView.setAdapter(adapter);
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new DBHelper(Profile.this).deleteAllData();

                AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
                builder.setTitle(R.string.Are_you_sure_do_you_want_to_Logout)
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getApplicationContext(),Profile.class);
                                startActivity(intent);
                                finish();

                            }
                        })
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new SharedPref(Profile.this).clearPref();
                                Intent intent = new Intent(getApplicationContext(), Language.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .show();



            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        findViewById(R.id.resetpassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ResetPassword.class);
                startActivity(intent);
            }
        });

    }
    public boolean isStoragePermissionGranted() {
        String TAG = "Permisiion";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 0);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }
    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                if (isStoragePermissionGranted())
                    launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }



    private void launchCameraIntent() {
        Intent intent = new Intent(Profile.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, 1012);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(Profile.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, 1012);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1012) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                Log.i("kishore",RealPathUtil.getRealPath(Profile.this,uri));
                new uploadTask().execute(RealPathUtil.getRealPath(Profile.this,uri));
            }
        }
    }
    private void intiViews() {
        checks=new StaticChecks(this);
        pref=new SharedPref(this);
        recyclerView=findViewById(R.id.profile_recycle);
        TextView username=findViewById(R.id.username);
        if (pref.getString("username").length()>15){
            if (pref.getString("username").contains(" ")){
                String[] names=pref.getString("username").split(" ");
                String namestr="";
                for (String nam:names){
                    namestr+=nam+"\n";
                }
                username.setText(namestr);
            }else{
                username.setText(pref.getString("username"));
            }
        }else {
            username.setText(pref.getString("username"));
        }
        type=pref.getString("type");
        TextView welcome=findViewById(R.id.welcome);
        checks.setWishes(welcome);
        profileImage=findViewById(R.id.profile_image);
        Glide.with(this).load(pref.getString("ProfileImage"))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.vanashreelogo).into(profileImage);
        Log.i("image url",pref.getString("ProfileImage"));
    }

    private class uploadTask extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("",strings[0],
                            RequestBody.create(MediaType.parse("application/octet-stream"),
                                    new File(strings[0])))
                    .build();
            Request request = new Request.Builder()
                    .url(getString(R.string.baseURL)+"ForestApi/API/ImageUpload/Upload?imgname="+id+"&id="+id+"&type="+type)
                    .method("POST", body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("true")){
                checks.showSnackBar(getResources().getString(R.string.sucess));
                pref.addString("ProfileImage",getString(R.string.baseURL)+"NTFPProfilePictures/"+id+".jpg");
                Glide.with(Profile.this).load(pref.getString("ProfileImage"))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .error(R.drawable.vector_account_circle)
                        .into(profileImage);
            }else{
                SnackBarUtils.ErrorSnack(Profile.this,s);
            }
            findViewById(R.id.spin_kit).setVisibility(View.GONE);
        }
    }
}