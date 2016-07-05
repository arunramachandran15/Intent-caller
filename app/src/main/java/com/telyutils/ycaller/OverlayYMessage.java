package com.telyutils.ycaller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.telyutils.retro.content.GeneralResponse;
import com.telyutils.retro.content.ReceiveCall;
import com.telyutils.retro.interfaces.YCallerService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ankit on 6/28/2016.
 */
public class OverlayYMessage extends Activity implements Callback<GeneralResponse> {

    private static final String TAG = OverlayYMessage.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String state = getIntent().getStringExtra(TelephonyManager.EXTRA_STATE);
        //incoming call fetch Ymessage if any
        Log.d(TAG, "state "+state);
        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)){

            Log.e(TAG, "Inside EXTRA_STATE_RINGING");
            String number = getIntent().getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.e(TAG, "incoming number : " + number);
            getYMessage(number);

        }else {
            finish();
        }
    }

    private void getYMessage(String number){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YCallerService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        final SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String myPhoneNumber = sharedpreferences.getString("mynumber", "");
        Log.d(TAG,"Myphone number "+myPhoneNumber);
        ReceiveCall receiveCall = new ReceiveCall(number,myPhoneNumber);
        YCallerService service = retrofit.create(YCallerService.class);
        Call<GeneralResponse> generalResponse = service.receiveCall(receiveCall);
        generalResponse.enqueue(this);
    }


    private void displayAlert(String yMessage) {
        Log.d(TAG, "YMessage"+yMessage);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(yMessage).setCancelable(
                false).setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
        if(response.body().getStatuscode() == 201) {
            Log.d(TAG, "Success receiveCall");
            displayAlert(response.body().getDescription());
        }
    }

    @Override
    public void onFailure(Call<GeneralResponse> call, Throwable t) {
        finish();
        //do nothing
    }
}


