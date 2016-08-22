/*
 * Copyright (c) 2016. Ramanathan
 */

package com.telyutils.ycaller;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.telyutils.adapter.ContactAdapter;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends Activity {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private Button showContacts;
    private ListView lstNames;
    private ImageView normalCall;
    private ImageView ycall;
    List<CustomerDao> contactsList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        initComponents();
        importContacts();
        lstNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                CustomerDao notification = contactsList.get(position);

                Intent ycall = new Intent(ContactActivity.this, MainActivity.class);
                ycall.putExtra("number",notification.customer_No);
                startActivity(ycall);
                Toast.makeText(ContactActivity.this, notification.customer_Name
                        , Toast.LENGTH_LONG).show();

            }
        });

    }


    private void initComponents() {
        lstNames = (ListView) findViewById(R.id.lstNames);

    }


    private void importContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            List<CustomerDao> contacts = getContactNames();
            contactsList = contacts;
           // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_contact_list, contacts);
            ContactAdapter contactAdapter=new ContactAdapter(this,R.layout.custom_contact_list,contacts);
            lstNames.setAdapter(contactAdapter);


        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                importContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private List<CustomerDao> getContactNames() {
        List<CustomerDao> contacts = new ArrayList<>();
        // Get the ContentResolver
        ContentResolver cr = getContentResolver();
        Cursor  cursor=null;
        try {
            cursor = getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            int contactIdIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);
            int nameIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int phoneNumberIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int photoIdIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID);
            cursor.moveToFirst();
            do {
                int idContact = cursor.getInt(contactIdIdx);
                String name = cursor.getString(nameIdx);
                String phoneNumber = cursor.getString(phoneNumberIdx);
                contacts.add(new CustomerDao(name,phoneNumber,idContact));

            } while (cursor.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }


        return contacts;
    }

}



