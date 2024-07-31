package com.gisfy.ntfp.HomePage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gisfy.ntfp.Collectors.CollectorInventory;
import com.gisfy.ntfp.Collectors.Collector_Pass;
import com.gisfy.ntfp.Collectors.InventoryList;
import com.gisfy.ntfp.Collectors.PDFList;
import com.gisfy.ntfp.Collectors.PriceListPage;
import com.gisfy.ntfp.Collectors.StockRequest;
import com.gisfy.ntfp.Collectors.Ticket_UI;
import com.gisfy.ntfp.Collectors.VideoGallaryActivity;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.RFO.Status.ToDFO;
import com.gisfy.ntfp.RFO.Status.ToVSS;
import com.gisfy.ntfp.RFO.Status.TransitActivity;
import com.gisfy.ntfp.Utils.Constants;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.VSS.Collectors.ImportExcel;
import com.gisfy.ntfp.VSS.Collectors.add_collector;
import com.gisfy.ntfp.VSS.Collectors.list_collectors;
import com.gisfy.ntfp.VSS.Dashboard.Dashboard;
import com.gisfy.ntfp.VSS.Inventory.add_inventory;
import com.gisfy.ntfp.VSS.Inventory.list_inventory;
import com.gisfy.ntfp.VSS.Payment.MakePaymentsActivity;
import com.gisfy.ntfp.VSS.Payment.Recieved.ReceivedPaymentsActivity;
import com.gisfy.ntfp.VSS.RequestForm.list_request;
import com.gisfy.ntfp.VSS.RequestForm.view_requests;
import com.gisfy.ntfp.VSS.Shipment.add_shipment;
import com.gisfy.ntfp.VSS.Shipment.list_shipment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFrag2 extends Fragment {


    private final int state;
    private final String title;
    private RecyclerView quickRecycle;
    private SharedPref pref;
    private HomeAdapter standardAdapter;
    List<HomeModel> standardModels=new ArrayList<>();
    private ImageView back;
    public HomeFrag2(int state,String title){
        this.state=state;
        this.title=title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_home_frag2, container, false);
        quickRecycle=v.findViewById(R.id.standard_recycle);
        pref=new SharedPref(getContext());
        back=v.findViewById(R.id.back);
        TextView titletv=v.findViewById(R.id.standard_recycle_tv);
        titletv.setText(title);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.home_fram_layout, new HomeFrag1());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        initRecycle();
        return v;
    }

    private void initRecycle() {
        quickRecycle.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        standardAdapter=new HomeAdapter(standardModels, (Home) getActivity(),1);
        quickRecycle.setAdapter(standardAdapter);
        quickRecycle.smoothScrollToPosition(0);
        switch (pref.getString("type")) {
            case Constants.VSS:
                switch (state) {
                    case 1:
                        standardModels.add(new HomeModel(getString(R.string.addcollector), R.drawable.vector_add_green, add_collector.class, false,""));
                        standardModels.add(new HomeModel(getString(R.string.importexcel), R.drawable.excel_icon, ImportExcel.class, false,""));
                        standardModels.add(new HomeModel(getString(R.string.viewcollecotrs), R.drawable.vector_view, list_collectors.class, false,""));
                        standardAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        clearList();
                        standardModels.add(new HomeModel(getString(R.string.addinv), R.drawable.vector_add_green, add_inventory.class, false,""));
                        standardModels.add(new HomeModel(getString(R.string.viewstocks), R.drawable.vector_view, list_inventory.class, false,""));
                        standardAdapter.notifyDataSetChanged();
                        break;
                    case 3:
                        clearList();
                        standardModels.add(new HomeModel(getString(R.string.addtransitrequest), R.drawable.vector_add_green, list_request.class, false,""));
                        standardModels.add(new HomeModel(getString(R.string.viewtransit), R.drawable.vector_view, view_requests.class, false,""));
                        standardAdapter.notifyDataSetChanged();
                        break;
                    case 4:
                        clearList();
                        standardModels.add(new HomeModel(getString(R.string.addshipment), R.drawable.vector_add_green, add_shipment.class, false,""));
                        standardModels.add(new HomeModel(getString(R.string.viewshipment), R.drawable.vector_view, list_shipment.class, false,""));
                        standardAdapter.notifyDataSetChanged();
                        break;
                    case 5:
                        clearList();
                        standardModels.add(new HomeModel(getString(R.string.recievedpayments), R.drawable.vector_payment_green, ReceivedPaymentsActivity.class, false,""));
                        standardModels.add(new HomeModel(getString(R.string.newpayments), R.drawable.vector_payment_red, MakePaymentsActivity.class, false,""));
                        standardAdapter.notifyDataSetChanged();
                        break;
                    case 0:
                        startActivity(new Intent(getContext(), Dashboard.class));
                        break;
                }
                break;
            case Constants.RFO:
                switch (state) {
                    case 0:
                        startActivity(new Intent(getContext(), com.gisfy.ntfp.RFO.Dashboard.class));
                        break;
                    case 1:
                        startActivity(new Intent(getContext(), TransitActivity.class));
                        break;
                    case 2:
                        clearList();
                        standardModels.add(new HomeModel(getString(R.string.todfo), R.drawable.vector_acknowledgement, ToDFO.class, false,""));
                        standardModels.add(new HomeModel(getString(R.string.tovss), R.drawable.vector_acknowledgement, ToVSS.class, false,""));
                        standardAdapter.notifyDataSetChanged();
                        break;
                }
                break;
            case Constants.COLLECTORS:
                switch (state) {
                    case 2:
                        clearList();
//                        standardModels.add(new HomeModel(getString(R.string.pass), R.drawable.vector_pass, Collector_Pass.class, false));
                        standardModels.add(new HomeModel(getString(R.string.idcard), R.drawable.vector_id_card, Ticket_UI.class, false,""));
                        standardAdapter.notifyDataSetChanged();
                        break;
                    case 3:
                        clearList();
                        standardModels.add(new HomeModel("Pricing List", R.drawable.vector_payment_green, PriceListPage.class,false,""));
                        standardModels.add(new HomeModel(getString(R.string.materials), R.drawable.vector_pdf, PDFList.class, false,""));
                        standardModels.add(new HomeModel(getString(R.string.tutorials), R.drawable.vector_tutorials,VideoGallaryActivity.class, false,""));
                        standardAdapter.notifyDataSetChanged();
                        break;

                    case 0:
                        clearList();
                        standardModels.add(new HomeModel(getString(R.string.addinv), R.drawable.vector_add_green, CollectorInventory.class, false,""));
                        standardModels.add(new HomeModel(getString(R.string.viewstocks), R.drawable.vector_view, InventoryList.class, false,""));
                        standardModels.add(new HomeModel(getString(R.string.stocktovss), R.drawable.vector_view, StockRequest.class, false,""));
                        standardAdapter.notifyDataSetChanged();
                        break;

                }
                break;
        }
    }
    public void clearList(){
        int size = standardModels.size();
        standardModels.clear();
        standardAdapter.notifyItemRangeRemoved(0, size);
    }
}