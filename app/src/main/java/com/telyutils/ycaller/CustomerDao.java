/*
 * Copyright (c) 2016. Ramanathan
 */

package com.telyutils.ycaller;

/**
 * Created by viki on 10/7/16.
 */
public class CustomerDao {
    public String customer_Name;
    public String customer_No;
    public int customer_icon;




    public CustomerDao(String customer_Name, String customer_No, int customer_icon) {
        this.customer_Name = customer_Name;
        this.customer_No = customer_No;
        this.customer_icon = customer_icon;
    }


    public String getCustomer_No() {
        return customer_No;
    }

    public void setCustomer_No(String customer_No) {
        this.customer_No = customer_No;
    }

    public String getCustomer_Name() {
        return customer_Name;
    }

    public void setCustomer_Name(String customer_Name) {
        this.customer_Name = customer_Name;
    }

    public int getCustomer_icon() {
        return customer_icon;
    }

    public void setCustomer_icon(int customer_icon) {
        this.customer_icon = customer_icon;
    }


}
