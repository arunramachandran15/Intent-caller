package com.telyutils.ycaller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by ankit on 6/28/2016.
 */
public class OverlayYMessage extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            try {

            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

            setContentView(R.layout.activity_overlay_message);

            String number = getIntent().getStringExtra(
                    TelephonyManager.EXTRA_INCOMING_NUMBER);
            TextView yMessageText = (TextView) findViewById(R.id.tViewYMessage);
            yMessageText.setText("Here goes YMessage" + number);
        }
        catch (Exception e) {
            Log.d("Exception", e.toString());
            e.printStackTrace();
        }
    }
}


