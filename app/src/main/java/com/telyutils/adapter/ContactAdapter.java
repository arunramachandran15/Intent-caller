/*
 * Copyright (c) 2016. Ramanathan
 */

package com.telyutils.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.telyutils.ycaller.ContactActivity;
import com.telyutils.ycaller.CustomerDao;
import com.telyutils.ycaller.R;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends ArrayAdapter<CustomerDao> {


    private Context context;
    private List<CustomerDao> contactList = new ArrayList<>();



    public ContactAdapter(ContactActivity context, int custom_contact_list, List<CustomerDao> contacts) {
        super(context, custom_contact_list, contacts);
        this.context = context;
        this.contactList = contacts;
    }

    private class ViewHolder {

        TextView customer_Name;
        TextView customer_Number;
        ImageView customerImage;
        private ImageView normalCall;
        private ImageView ycall;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_contact_list, null);
            holder = new ViewHolder();
            holder.customer_Name = (TextView) convertView.findViewById(R.id.con_name);
            holder.customer_Number = (TextView) convertView.findViewById(R.id.con_no);
            holder.customerImage = (ImageView) convertView.findViewById(R.id.logo);
            holder.normalCall=(ImageView)convertView.findViewById(R.id.normal_callimg);
            holder.ycall=(ImageView)convertView.findViewById(R.id.ycaller_img);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();

        }
        CustomerDao custobj = contactList.get(position);
        String nameee = custobj.getCustomer_Name();
        Log.d("Tagg",nameee);
        holder.customer_Name.setText(nameee);
        holder.customer_Number.setText(contactList.get(position).getCustomer_No());

        return convertView;
    }

}
