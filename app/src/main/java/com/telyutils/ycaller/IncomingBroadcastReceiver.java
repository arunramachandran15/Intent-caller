package com.telyutils.ycaller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by ankit on 6/28/2016.
 */
public class IncomingBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = IncomingBroadcastReceiver.class.getSimpleName();
    @Override
    public void onReceive(final Context context, final Intent intent) {

        Log.d("Ringing", "Something Happened");
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)){

            Log.e(TAG, "Inside EXTRA_STATE_RINGING");
            String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.e(TAG, "incoming number : " + number);

        }


        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)
                || state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {

            Log.d("Ringing", "Phone is ringing");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(context, OverlayYMessage.class);
                    i.putExtras(intent);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            }, 0);

        }
    }
}