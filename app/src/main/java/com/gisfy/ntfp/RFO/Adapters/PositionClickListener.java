package com.gisfy.ntfp.RFO.Adapters;

import com.gisfy.ntfp.RFO.Models.DFOACKModel;
import com.gisfy.ntfp.RFO.Models.TransitPassModel;
import com.gisfy.ntfp.RFO.Models.VSSACKModel;

public interface PositionClickListener
{
    void itemClicked(TransitPassModel position);
    void itemClicked(DFOACKModel position);
    void itemClicked(VSSACKModel position);
}