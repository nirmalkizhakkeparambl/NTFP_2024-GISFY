package com.gisfy.ntfp.VSS.Dashboard;

import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;

public class MyValueFormatter implements ValueFormatter {

        private final String mFormat="";

        public MyValueFormatter() {

        }

        @Override
        public String getFormattedValue(float value) {
            if (value/100==(int)value)
                return String.valueOf(value);
            else
                return "";
        }
    }