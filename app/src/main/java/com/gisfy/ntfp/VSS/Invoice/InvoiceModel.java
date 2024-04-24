package com.gisfy.ntfp.VSS.Invoice;


public class InvoiceModel {
    public String id;
    public String col;
    public String col2;
    public String col3;
    public String subcol3;
    public String col4;
    public String subcol4;

    public InvoiceModel() {
    }

    public InvoiceModel(String id, String col, String col2, String col3, String subcol3, String col4, String subcol4) {
        this.id = id;
        this.col = col;
        this.col2 = col2;
        this.col3 = col3;
        this.subcol3 = subcol3;
        this.col4 = col4;
        this.subcol4 = subcol4;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public String getCol2() {
        return col2;
    }

    public void setCol2(String col2) {
        this.col2 = col2;
    }

    public String getCol3() {
        return col3;
    }

    public void setCol3(String col3) {
        this.col3 = col3;
    }

    public String getSubcol3() {
        return subcol3;
    }

    public void setSubcol3(String subcol3) {
        this.subcol3 = subcol3;
    }

    public String getCol4() {
        return col4;
    }

    public void setCol4(String col4) {
        this.col4 = col4;
    }

    public String getSubcol4() {
        return subcol4;
    }

    public void setSubcol4(String subcol4) {
        this.subcol4 = subcol4;
    }
}
