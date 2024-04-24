package com.gisfy.ntfp.HomePage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gisfy.ntfp.Collectors.Payments.CollectorPaymentsActivity;
import com.gisfy.ntfp.Profile.Profile;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.RFO.RFOProfile;
import com.gisfy.ntfp.RFO.Status.ToDFO;
import com.gisfy.ntfp.RFO.Status.ToVSS;
import com.gisfy.ntfp.RFO.Status.TransitActivity;
import com.gisfy.ntfp.Utils.Constants;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.VSS.CollectorInventory.CollectorInventory;
import com.gisfy.ntfp.VSS.Dashboard.Dashboard;
import com.gisfy.ntfp.VSS.PCtoVSS.PCTOVSSActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class HomeFrag1 extends Fragment {

    private RecyclerView quickRecycle;
    private SharedPref pref;
    public HomeFrag1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_home_frag1, container, false);
        quickRecycle=v.findViewById(R.id.standard_recycle);
        pref=new SharedPref(getContext());
        initQuick();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initQuick() {
        List<HomeModel> homeModels = new ArrayList<>();
        if (pref.getString("type").equals(Constants.VSS)) {
            homeModels.add(new HomeModel(getString(R.string.dashboard), R.drawable.vector_dashboard, Dashboard.class, false,""));
            homeModels.add(new HomeModel(getString(R.string.collectors), R.drawable.vector_peoples, null, false,""));
            homeModels.add(new HomeModel(getString(R.string.inventory), R.drawable.vector_inventory, null, false,""));
            homeModels.add(new HomeModel(getString(R.string.transitrequest), R.drawable.vector_transit, null, false,""));
            homeModels.add(new HomeModel(getString(R.string.shipment), R.drawable.vector_transport, null, false,""));
            homeModels.add(new HomeModel(getString(R.string.payment), R.drawable.vector_credit_24, null, false,""));
            homeModels.add(new HomeModel(getString(R.string.pcack), R.drawable.vector_acknowledgement, PCTOVSSActivity.class, false,""));
            homeModels.add(new HomeModel(getString(R.string.ntfpdeposits), R.drawable.vector_inventory, CollectorInventory.class, false,""));
            homeModels.add(new HomeModel(getString(R.string.profile), R.drawable.profileperson_outline_24, Profile.class, false,""));
        }
        else if (pref.getString("type").equals(Constants.RFO)){
            homeModels.add(new HomeModel(getString(R.string.dashboard), R.drawable.vector_dashboard, com.gisfy.ntfp.RFO.Dashboard.class, false,""));
            homeModels.add(new HomeModel(getString(R.string.transitrequest), R.drawable.vector_transit, TransitActivity.class, false,""));
            homeModels.add(new HomeModel(getString(R.string.processing_centre_confirmation), R.drawable.vector_acknowledgement, ToDFO.class, false,""));
            homeModels.add(new HomeModel(getString(R.string.profile), R.drawable.profileperson_outline_24, RFOProfile.class, false,""));
        }
        else if (pref.getString("type").equals(Constants.COLLECTORS)){
            homeModels.add(new HomeModel(getString(R.string.inventory), R.drawable.vector_inventory, null, false,""));
            homeModels.add(new HomeModel(getString(R.string.payment), R.drawable.vector_credit_24, CollectorPaymentsActivity.class, false,""));
            homeModels.add(new HomeModel(getString(R.string.passes), R.drawable.vector_pass, null, false,""));
            homeModels.add(new HomeModel(getString(R.string.learnings), R.drawable.vector_learnings, null, false,""));
            homeModels.add(new HomeModel(getString(R.string.profile), R.drawable.profileperson_outline_24, Profile.class, false,""));
        }
        quickRecycle.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        quickRecycle.setAdapter(new HomeAdapter(homeModels, (Home) getActivity(),1));
    }
}