package com.gisfy.ntfp.VSS.RequestForm;

import android.Manifest;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gisfy.ntfp.R;
import com.gisfy.ntfp.RFO.Models.TransitNTFPModel;
import com.gisfy.ntfp.RFO.Models.TransitPassModel;
import com.gisfy.ntfp.SqliteHelper.Entity.MemberModel;
import com.gisfy.ntfp.SqliteHelper.NtfpDao;
import com.gisfy.ntfp.SqliteHelper.SynchroniseDatabase;
import com.gisfy.ntfp.Utils.PermissionUtils;
import com.gisfy.ntfp.VSS.Shipment.add_shipment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


public class adapter_request_list  extends RecyclerView.Adapter<adapter_request_list.ViewHolder> {
    File directory, sd, file;
    WritableWorkbook workbook;
    view_requests activity;
    private List<TransitPassModel> inventoryList;
    PermissionUtils permissionUtils;
    private NtfpDao dao;
    private MemberModel memberModel = null;

    public adapter_request_list(List<TransitPassModel> inventoryList, view_requests activity) {
        this.inventoryList = inventoryList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_requested, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final TransitPassModel data = inventoryList.get(position);
        holder.title1.setText(data.getTransUniqueId());
        holder.title2.setText(data.getpCName());

        String dateget = data.getDate();
        String[] dateParts = dateget.split("-");
        String year = dateParts[0];
        String month = dateParts[1];
        String day = dateParts[2];
        String datanew = day+"-"+ month+"-"+year;
        holder.date.setText(datanew);
        Log.i("datagetMemberID",data.getMemberID()+"");


        holder.more.setVisibility(View.GONE);

        if (data.getTransitStatus().equals("Accepted")&&data.getShipmentStatus().equals("Shipped")){
            holder.shipLayout.setVisibility(View.INVISIBLE);
            holder.downloadLayout.setVisibility(View.VISIBLE);
            holder.statusimage.setImageResource(R.drawable.vector_check_green);
            holder.status.setText(activity.getString(R.string.acceptedandshipped));
        }else if (data.getTransitStatus().equals("Accepted")&&data.getShipmentStatus().equals("Pending")){
            holder.shipLayout.setVisibility(View.INVISIBLE);
            holder.downloadLayout.setVisibility(View.VISIBLE);
            holder.statusimage.setImageResource(R.drawable.vector_check_green);
            holder.status.setText(activity.getString(R.string.accepted));
            holder.shipLayout.setVisibility(View.VISIBLE);
            holder.downloadLayout.setVisibility(View.VISIBLE);
        }else if (data.getTransitStatus().equals("Rejected")){
            holder.shipLayout.setVisibility(View.INVISIBLE);
            holder.downloadLayout.setVisibility(View.INVISIBLE);
            holder.statusimage.setImageResource(R.drawable.vector_clear_red);
            holder.status.setText(activity.getString(R.string.rejected));
        }else{
            holder.shipLayout.setVisibility(View.INVISIBLE);
            holder.downloadLayout.setVisibility(View.INVISIBLE);
            holder.statusimage.setImageResource(R.drawable.vector_time);
            holder.status.setText(activity.getString(R.string.pending));
        }


        holder.longPresslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] titles = activity.getResources().getStringArray(R.array.transit_titles3);
                List<List<String>> rows = new ArrayList<>();
                for (TransitNTFPModel model:data.getnTFP()){
                    List<String> row = new ArrayList<>();
                    row.add(model.getnTFPmalayalamname()+"("+model.getnTFPName().split("-")[0]+")");
                    row.add(model.getQuantity()+" "+model.getUnit());
                    dao = SynchroniseDatabase.getInstance(v.getContext()).ntfpDao();
                    memberModel = dao.getMemberFromMemberId(data.getMemberID());
                    try {
                        if (memberModel.getName()!=null){
                            String memberName = memberModel.getName();
                            Log.i("memberName126",memberName);
                            row.add(memberName);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    rows.add(row);
                }
                new ChartAlert(activity,titles,rows).show();
            }
        });
        holder.shipLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(activity, add_shipment.class);
                i.putExtra("pass",data.getTransUniqueId());
                i.putExtra("date",data.getDate());
                i.putExtra("pc",data.getpCName());
                activity.startActivity(i);
            }
        });

        holder.downloadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                permissionUtils = new PermissionUtils();
                if (permissionUtils.checkPermission(activity, 1, view)) {
                     try {
                         Log.i("datagetTransUniqueId",data.getTransUniqueId()+"");
                         activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://vanasree.com/NTFPIMS/VSSTransitPass.aspx?TransUniqueId="+data.getTransUniqueId())));
                     } catch (Exception e) {
                         e.getStackTrace();
                     }
                }
            }
        });
    }

    private void createExcelSheet(String transid, TransitPassModel data) {
        String csvFile = transid+".xls";
        sd = Environment.getExternalStorageDirectory();
        directory = new File(sd.getAbsolutePath()+"/Download");
        file = new File(directory, csvFile);
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        try {
            workbook = jxl.Workbook.createWorkbook(file, wbSettings);
            createFirstSheet(data);
            //closing cursor
            workbook.write();
            Toast.makeText(activity, "Saved in your Downloaded File", Toast.LENGTH_SHORT).show();
            addNotification("Info with Transit No: "+transid+" is downloaded");
            workbook.close();
            Log.i("checkFile",file.exists()+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void addNotification(String msg) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(activity, "M_CH_ID");

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.download_24)
                .setTicker("Hearty365")
                .setPriority(Notification.PRIORITY_MAX) // this is deprecated in API 26 but you can still use for below 26. check below update for 26 API
                .setContentTitle("Default notification")
                .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .setContentInfo("Info");
        Intent i = new Intent();
        i.setComponent(new ComponentName(activity,DownloadManager.ACTION_VIEW_DOWNLOADS ));
        PendingIntent pIntent = PendingIntent.getActivity(activity, 0, i, 0);
        notificationBuilder.setContentIntent(pIntent);

        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());

    }
    void createFirstSheet(TransitPassModel data) {
        try {
            List<TransitNTFPModel> listdata = new ArrayList<>();
            for (TransitNTFPModel model:data.getnTFP()){
                listdata.add(new TransitNTFPModel(model.getnTFPName(),model.getnTFPmalayalamname(), model.getUnit(), model.getQuantity(), model.getMemberID()));
            }
            //Excel sheet name. 0 (number)represents first sheet
            WritableSheet sheet = workbook.createSheet("TransitNtfp", 0);
            // column and row title
            sheet.addCell(new Label(0, 0, "NTFPName"));
            sheet.addCell(new Label(1, 0, "NTFPmalayalamname"));
            sheet.addCell(new Label(2, 0, "Unit"));
            sheet.addCell(new Label(3, 0, "Quantity"));

            for (int i = 0; i < listdata.size(); i++) {
                sheet.addCell(new Label(0, i + 1, listdata.get(i).getnTFPName()));
                sheet.addCell(new Label(1, i + 1, listdata.get(i).getnTFPmalayalamname()));
                sheet.addCell(new Label(2, i + 1, listdata.get(i).getUnit()));
                sheet.addCell(new Label(3, i + 1, listdata.get(i).getQuantity()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return inventoryList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updateData(List<TransitPassModel> viewModels) {
        inventoryList.clear();
        inventoryList.addAll(viewModels);
        notifyDataSetChanged();
    }
    public void addItem(int position, TransitPassModel viewModel) {
        inventoryList.add(position, viewModel);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        inventoryList.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title1, title2, status,date;
        ImageView more,statusimage;
        ConstraintLayout longPresslayout;
        LinearLayout shipLayout,downloadLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title1 = itemView.findViewById(R.id.transactionName);
            title2 = itemView.findViewById(R.id.transactionDetail);
            status = itemView.findViewById(R.id.request_Status);
            date = itemView.findViewById(R.id.transactionTime);
            more = itemView.findViewById(R.id.more_options);
            statusimage = itemView.findViewById(R.id.transactionImage);
            longPresslayout = itemView.findViewById(R.id.longPresslayout);
            shipLayout = itemView.findViewById(R.id.shipLayout);
            downloadLayout = itemView.findViewById(R.id.downloadLayout);
        }
    }

}
