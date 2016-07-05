package com.telyutils.ycaller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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

public class MainActivity extends AppCompatActivity implements Callback<GeneralResponse> {

    private static final String TAG = MainActivity.class.getSimpleName();

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


    private String phoneNumber ;
    /**
     * this makes a native android call and broadcasts YMessage for the overlay
     *
     * @param phoneNumber
     * @param yMessage
     */
    private void placeCall(String phoneNumber, String yMessage) {
        this.phoneNumber = phoneNumber;
        //make phone call placed api call to inform an incoming call to the YCaller Server
        reportStartCallApi(phoneNumber,yMessage);
    }

    /**
     * reports the server about user call placed
     * @param phoneNumber
     * @param yMessage
     */
    private void reportStartCallApi(String phoneNumber,String yMessage){
        TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String myPhoneNumber = tMgr.getLine1Number();
        StartCall startCall = new StartCall(myPhoneNumber,phoneNumber,yMessage);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YCallerService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YCallerService service = retrofit.create(YCallerService.class);
        Call<GeneralResponse> generalResponse = service.startCall(startCall);
        generalResponse.enqueue(this);
    }

    @Override
    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
        Log.d(TAG,""+response.body().getStatuscode());
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            finish();
            return;
        }

        startActivity(intent);
        finish();
    }

    @Override
    public void onFailure(Call<GeneralResponse> call, Throwable t) {
        Log.d(TAG,"Failure");
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            finish();
            return;
        }

        startActivity(intent);

        finish();
    }
}
