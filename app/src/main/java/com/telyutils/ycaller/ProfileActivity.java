package com.telyutils.ycaller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.telyutils.retro.content.GeneralResponse;
import com.telyutils.retro.content.StandardCallback;
import com.telyutils.retro.content.StartCall;
import com.telyutils.retro.interfaces.YCallerService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity  {

    private static final String TAG = ProfileActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final EditText phoneText = (EditText) findViewById(R.id.eTextPhone);
        final EditText yMessageText = (EditText) findViewById(R.id.eTextYMessage);
        Button callBtn = (Button) findViewById(R.id.btnCall);
        Button btnGoBack = (Button) findViewById(R.id.btnGoBack);



        assert phoneText != null;
        phoneText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        Button btnSaveNumber = (Button) findViewById(R.id.btnSaveNumber);
        final SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        final EditText eYourNumber = (EditText) findViewById(R.id.eTextPhone);
        btnSaveNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eYourNumber1 = eYourNumber.getText().toString();

                if (eYourNumber1.isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Phone number cant be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if ( eYourNumber1.length() !=10) {
                    Toast.makeText(ProfileActivity.this, "Phone number should be 10 digit", Toast.LENGTH_LONG).show();
                    return;
                }
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("mynumber","+91"+eYourNumber1 );
                editor.commit();
            }
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }




}
