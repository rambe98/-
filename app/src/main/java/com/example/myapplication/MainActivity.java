package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public static Boolean ON_OFF = false;
    Button button_OnOff;
    String str_OnOff = "OFF";
    MyBroadcastReceiver br;
    EditText hour, min;
    TextView hourtv;
    int hourTime , minTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        br = new MyBroadcastReceiver();

        hour = findViewById(R.id.hour);
        min = findViewById(R.id.min);

        hourtv = findViewById(R.id.hourtv);


    }

    //버튼 클릭 리스너 정의
    class btnOnCLickListener implements Button.OnClickListener {
        btnOnCLickListener() {
            button_OnOff = findViewById(R.id.button_OnOff);
        }

        @Override
        public void onClick(View v) {
            if (!ON_OFF) { //state : off
                ON_OFF = Boolean.TRUE;
                str_OnOff = "ON";
                button_OnOff.setText(str_OnOff);
                hourTime = Integer.parseInt(hour.getText().toString());
                minTime = Integer.parseInt(min.getText().toString());
                final Timer timer = new Timer();
                TimerTask timertesk = new TimerTask() {
                    @Override
                    public void run() {
                        if(minTime != 0) {
                            //1초씩 감소
                            minTime--;

                            // 0분 이상이면
                        } else if(hourTime != 0) {
                            // 1분 = 60초
                            minTime = 60;
                            minTime--;
                            hourTime--;
                        }

                        if (minTime <= 9){
                            min.setText("0" + minTime);
                        }else{
                            min.setText(Integer.toString(minTime));
                        }
                        if (hourTime <= 9){
                            hour.setText("0" + hourTime);
                        }else{
                            hour.setText(Integer.toString(hourTime));
                        }

                        if (hourTime == 0 && minTime == 0){
                            timer.cancel();
                            ON_OFF = Boolean.FALSE;
                            str_OnOff = "OFF";
                            button_OnOff.setText(str_OnOff);
                            try{ //통화 종료를 위한 try catch 구문
                                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                                assert tm != null;
                                if(tm.getCallState() == TelephonyManager.CALL_STATE_OFFHOOK) { //만일 현재 상태가 통화중이라면
                                    Intent intent = new Intent("myApp.endCall.broadcast"); // 사용하고자 하는 action
                                    intent.putExtra("state", "OFFHOOK"); // 해당 action에 대한 추가 정보
                                    sendBroadcast(intent); //myApp.endCall.broadcast라는 action과 이에 대한 정보를 담아 broadcast

                                }

                            }catch (Exception e){
                                e.printStackTrace();

                            }
                        }
                    }
                };
                timer.schedule(timertesk, 0,1000); //타이머 실행
            }
        }
    }
    //

    // 실제 처음 실행되서 메모리에 생성될 때 발생하는 거임


    //앱이 실행되는 동안 수행되는 것
    protected void onResume() {
        super.onResume();

        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction("myApp.endCall.broadcast");
        registerReceiver(br, iFilter); // 리시버를 등록함

        btnOnCLickListener btn = new btnOnCLickListener();
        button_OnOff.setOnClickListener(btn);
    }

    //앱이 정지(중단)될 때
    protected void onPause() {
        unregisterReceiver(br); // 리시버를 제거함
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //빽(취소)키가 눌렸을때 종료여부를 묻는 다이얼로그 띄움
        if((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder d = new AlertDialog.Builder(MainActivity.this);
            d.setTitle("콜트롤러");
            d.setMessage("정말 종료 하시겠습니꺄?");

            d.setPositiveButton("예",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    MainActivity.this.finish();
                }
            });

            d.setNegativeButton("아니요",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.cancel();
                }
            });
            d.show();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}





