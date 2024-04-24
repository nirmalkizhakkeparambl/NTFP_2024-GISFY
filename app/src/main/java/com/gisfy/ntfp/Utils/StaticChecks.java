package com.gisfy.ntfp.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.gisfy.ntfp.Login.Models.VSSUser;
import com.gisfy.ntfp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.cardview.widget.CardView;

public class StaticChecks {
    Context context;
    public StaticChecks(Context context){
        this.context=context;
    }
    public static ViewPropertyAnimator slideOutToBottom(Context ctx, View view) {
        final int screenHeight = ctx.getResources().getDisplayMetrics().heightPixels;
        int[] coords = new int[2];
        view.getLocationOnScreen(coords);
        return view.animate().translationY(screenHeight - coords[0]).setDuration(2);
    }

    public static ViewPropertyAnimator slideInFromBottom(Context ctx, View view) {
        final int screenHeight = ctx.getResources().getDisplayMetrics().heightPixels;
        int[] coords = new int[2];
        view.getLocationOnScreen(coords);
        view.setTranslationY(screenHeight - coords[0]);
        return view.animate().translationY(0).setDuration(2).setInterpolator(new OvershootInterpolator(1f));
    }
    public boolean checkETList(TextInputEditText[] ets){
        boolean flag=true;
        for (TextInputEditText et:ets){
            if (et.length()<1){
                SnackBarUtils.ErrorSnack((Activity)context,et.getHint()+context.getString(R.string.required));
                flag=false;
                et.requestFocus();
                et.setError("Required Filed");
                break;
            }
        }
        return flag;
    }
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null
                , MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
    public static boolean isDateInRange(Date start,Date end,Date actual){
        return actual.compareTo(start) >= 0 && actual.compareTo(end) <= 0;
    }
    public void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
    public boolean checkETList(EditText[] ets){
        boolean flag=true;
        for (EditText et:ets){
            if (et.length()<1){
                SnackBarUtils.ErrorSnack((Activity)context,et.getHint()+context.getString(R.string.required));
                flag=false;
                et.setFocusable(true);
                break;
            }
        }
        return flag;
    }
    public static void setCardClick(CardView cardView, CardView[] cards){
        cardView.setCardElevation(20);
        cardView.setCardBackgroundColor(Color.GREEN);
        for (CardView card:cards){
            card.setCardElevation(10);
            card.setCardBackgroundColor(Color.WHITE);
        }
    }
    public RadioButton checkedTitle(RadioGroup group){
        int radioButtonID = group.getCheckedRadioButtonId();
        View radioButton = group.findViewById(radioButtonID);
        int idx = group.indexOfChild(radioButton);
        return (RadioButton) group.getChildAt(idx);
    }
    public boolean checkSpinnerList(Spinner[] ets){
        boolean flag=true;
        for (Spinner et:ets){
            if (et.getSelectedItemPosition()==0){
                SnackBarUtils.ErrorSnack((Activity)context,et.getSelectedItem().toString());
                et.requestFocus();
                flag=false;
                break;
            }
        }
        return flag;
    }

    public static boolean isValidAadharNumber(String str) {
        String regex = "^[2-9]{1}[0-9]{3}[0-9]{4}[0-9]{4}$";
        Pattern p = Pattern.compile(regex);
        if (str == null) {
            return false;
        }
        Matcher m = p.matcher(str);
        return m.matches();
    }
    public void setValues(TextInputEditText[] et){
        SharedPref pref=new SharedPref(context);
        VSSUser model=pref.getVSS();
        et[0].setEnabled(false);
        et[1].setEnabled(false);
        et[2].setEnabled(false);
        et[0].setText(model.getVid()+"");
        et[1].setText(model.getDivisionId()+"");
        et[2].setText(model.getRangeId()+"");
    }
    public void showSnackBar(String message){
        Activity activity = (Activity) context;

        if (null != activity && null != message) {
            final Snackbar snackbar=Snackbar.make(
                    activity.findViewById(android.R.id.content),
                    message, Snackbar.LENGTH_SHORT
            );

            final View snackbarView = snackbar.getView();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
            params.gravity = Gravity.TOP;
            snackbarView.setLayoutParams(params);
            snackbarView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_in_snack_bar));
            snackbar.setAction("ok", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbarView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_snack_bar));
                    snackbar.dismiss();
                }
            });
            snackbar.show();

        }
    }
    public void setWishes(TextView tv){
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if(timeOfDay >= 0 && timeOfDay < 12){
            tv.setText(context.getString(R.string.goodmorning));
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            tv.setText(context.getString(R.string.afternoon));
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            tv.setText(context.getString(R.string.evening));
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            tv.setText(context.getString(R.string.night));
        }
    }
    public void setTableLayout(TableLayout tableLayout,String[] titles, List<List<String>> rows){
        tableLayout.removeAllViews();
        SharedPref pref=new SharedPref(context);
        TableRow tbrow0 = new TableRow(context);
        for (String title : titles) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            TextView tv0 = new TextView(context);
            tv0.setText(title);
            tv0.setPadding(10, 5, 10, 10);
            tv0.setTextColor(Color.WHITE);
            tv0.setTextSize(18);
            tv0.setGravity(Gravity.CENTER);
            tv0.setShadowLayer(1, 0, 0, Color.BLACK);
            tv0.setBackgroundColor(context.getResources().getColor(R.color.silver));
            tbrow0.addView(tv0);
        }
        tableLayout.addView(tbrow0);

        for (int i = 0; i < rows.size(); i++) {
            TableRow tbrow = new TableRow(context);
            for (int j=0;j<rows.get(i).size();j++) {
                TextView t1v = new TextView(context);
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.weight=1;
                t1v.setPadding(10,5,10,5);
                t1v.setText(rows.get(i).get(j));
                t1v.setTextColor(Color.BLACK);
                t1v.setGravity(Gravity.CENTER);
                t1v.setTextSize(15);
                t1v.setBackgroundColor(context.getResources().getColor(R.color.light_grey));
                tbrow.addView(t1v);
            }
            tableLayout.addView(tbrow);
        }
    }
    public static Date stringtoDate(String date){
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
//            SimpleDateFormat formatter= new SimpleDateFormat("dd/MM/yyyy");
            return new Date();
        }
    }

}
