package com.prembros.symptomator;

import android.content.Context;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

public class speed extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed);
    }

    public void sped(){
        final TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        final AudioManager audioManager=(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        telephony.listen(new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
//                LocationService ls=new LocationService();
//                Toast.makeText(context,"incoming : "+incomingNumber,Toast.LENGTH_LONG).show();

                if(state==1){

                    audioManager.setRingerMode(1);
                    if(!incomingNumber.equals("")){
                        //telephonyService.endCall();
                        SmsManager smsManager=SmsManager.getDefault();
                        smsManager.sendTextMessage(incomingNumber, null, "Driver is busy", null, null);}

                }
//                Toast.makeText(context,"incomingNumber : "+incomingNumber,Toast.LENGTH_LONG).show();

            }

        },PhoneStateListener.LISTEN_CALL_STATE);

    }
}
