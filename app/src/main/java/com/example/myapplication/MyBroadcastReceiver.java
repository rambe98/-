package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.ResourceBundle;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras(); // 현재 action에 대한 extra(추가정보)를 받아옴
        if (intent.getAction().equals("myApp.endCall.broadcast")) {
            TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
            if (extras != null) {
                String state = extras.getString("state"); //추가 정보 중 'state'값을 가져옴
                assert state != null;
                if (state.equals("OFFHOOK")) { // CALL_STATE_OFFHOOK --> 통화중이라면
                    assert telecomManager != null;
                    telecomManager.endCall(); // 통화 종료
                }
            }
        }
    };
}
//