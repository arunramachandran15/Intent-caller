package com.telyutils.ycaller;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Callback<GeneralResponse> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 1000;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1001;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 5000;
    private StartCall startCall;
    private String yMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText phoneText = (EditText) findViewById(R.id.eTextPhone);
        final EditText yMessageText = (EditText) findViewById(R.id.eTextYMessage);
        Button callBtn = (Button) findViewById(R.id.btnCall);
        Button ediprofileBtn = (Button) findViewById(R.id.btnEditProfile);

        assert phoneText != null;
        phoneText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        assert callBtn != null;
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String yMessage = yMessageText.getText().toString();
                String phoneNumber = phoneText.getText().toString();

                if (phoneNumber.isEmpty()|| phoneNumber.length()!=10) {
                    Toast.makeText(MainActivity.this, "Enter number tobe called, it cant be blank", Toast.LENGTH_LONG).show();
                    return;
                }
                phoneNumber =  "+91"+ phoneNumber;
                //if no message typed do not place a call
                if (yMessage.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter your YMessage, it cant be blank", Toast.LENGTH_LONG).show();
                    return;
                }

                placeCall(phoneNumber, yMessage);
            }
        });


        assert ediprofileBtn != null;
        ediprofileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

    }


    private String phoneNumber;

    /**
     * this makes a native android call and broadcasts YMessage for the overlay
     *
     * @param phoneNumber
     * @param yMessage
     */
    private void placeCall(String phoneNumber, String yMessage) {
        this.phoneNumber = phoneNumber;
        this.yMessage = yMessage;
        //make phone call placed api call to inform an incoming call to the YCaller Server
        reportStartCallApi();
    }

    /**
     * reports the server about user call placed
     */
    private void reportStartCallApi() {

        final SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String myPhoneNumber = sharedpreferences.getString("mynumber", "");
        startCall = new StartCall(myPhoneNumber, phoneNumber, yMessage);


        int permissionInternet = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);
        int phoneStatePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int callPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionInternet != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }

        if (phoneStatePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (callPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }

        if (listPermissionsNeeded.isEmpty()) {
            //already have all permissions
            makeApiCall();
            return;
        }

        // if some permissions missing request for permissions
        ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);

    }


    private void makeApiCall() {

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

        Log.d(TAG, "" + response.body().getStatuscode());
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
        Log.d(TAG, "Failure");
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            finish();
            return;
        }

        startActivity(intent);

        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull
                                           int[] grantResults) {

        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    makeApiCall();
                } else {
                    // permission denied, boo! Disable the
                    Toast.makeText(MainActivity.this, "Sorry Internet permission need to make a YCall"
                            , Toast.LENGTH_LONG).show();
                }

                break;
            }

            //this will work, multiple permissions to be asked
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.INTERNET, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                        Log.d(TAG, "all permissions granted");
                        makeApiCall();

                        //else any one or both the permissions are not granted
                    } else {

                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                            showDialogOK("Phone call and phone state permissions required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    reportStartCallApi();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    Toast.makeText(MainActivity.this, "No call placed", Toast.LENGTH_LONG).show();
                                                    finish();
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Get lost if u are not giving me permissions", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
            }
        }

    }

    /**
     *
     * @param message
     * @param listener
     */
    private void showDialogOK(String message, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton("Ok",listener);
        builder.setNegativeButton("Cancel",listener);
        AlertDialog alert = builder.create();
        alert.show();
    }

}

