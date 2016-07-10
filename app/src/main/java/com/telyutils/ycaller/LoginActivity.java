/*
 * Copyright (c) 2016. Ramanathan
 */

package com.telyutils.ycaller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponents();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToContact=new Intent(LoginActivity.this,ContactActivity.class);
                startActivity(moveToContact);
            }
        });

    }

    private void initComponents() {
        loginButton=(Button)findViewById(R.id.button);
    }




}
