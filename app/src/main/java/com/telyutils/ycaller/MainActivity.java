package com.telyutils.ycaller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText phoneText = (EditText) findViewById(R.id.eTextPhone);
        final EditText yMessageText = (EditText) findViewById(R.id.eTextYMessage);
        Button callBtn = (Button) findViewById(R.id.btnCall);

        assert phoneText != null;
        phoneText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        assert callBtn != null;
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String yMessage = yMessageText.getText().toString();
                String phoneNumber = phoneText.getText().toString();

                if (phoneNumber.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter number tobe called, it cant be blank", Toast.LENGTH_LONG).show();
                    return;
                }
                //if no message typed do not place a call
                if (yMessage.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter your fucking YMessage, it cant be blank", Toast.LENGTH_LONG).show();
                    return;
                }

                placeCall(phoneNumber, yMessage);
            }
        });
    }


    /**
     * this makes a native android call and broadcasts YMessage for the overlay
     * @param phoneNumber
     * @param yMessage
     */
    private void placeCall(String phoneNumber, String yMessage) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        startActivity(intent);
    }
}
