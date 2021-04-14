package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class IntroActivity extends AppCompatActivity {
    private Handler h;
    Runnable r = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent); // MainActivity로 화면 전환
            finish(); // IntroActivity 화면 제거
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_layout);

        // 앱 권환 확인
        if(!hasPermissions()) requestPermissions(); //만일 권한이 없는 경우 권한 요청
        else init(true);
    }
    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }

    final int REQ_PERMISSION_ALL = 1000;

    String[] permissions = { // 요청할 권한을 담고 있는 문자열 배열 선언
            Manifest.permission.MANAGE_OWN_CALLS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ANSWER_PHONE_CALLS
    };

    private boolean hasPermissions() { // 권한이 존재하는지 확인 ( 권한이 존재하지 않으면 false, 존재하면 true 반환 )
        for (String permission : permissions) {
            if(ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;}
        }
        return true;
    }

    private void requestPermissions() { // 권한 요청
        ActivityCompat.requestPermissions(this, permissions, REQ_PERMISSION_ALL);
    }
    public void onRequestPermissionsResult(int permissionCode, String permissions[], int[] grantResults) {
        try {
            switch (permissionCode) {
                case REQ_PERMISSION_ALL: {
                    boolean allPermissionEnabled = true;
                    for (int grandResult : grantResults) {
                        if (grandResult != PackageManager.PERMISSION_GRANTED)
                            allPermissionEnabled = false; // 만일 권한을 승인하지 않은 경우 false 저장
                    }
                    init(allPermissionEnabled); // 모든 권한이 승인됐는지에 대한 결과(boolean 값)를 넘겨줌
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void init(final boolean bool) { // 앱 초기화면 설정
        //화면 전환하는 함수 실행 ( 메인으로 가기 )
        if(bool){ // true(권한이 모두 승인된 경우)
            h = new Handler();
            h.postDelayed(r, 500); //0.5초(500ms) 이후 r 수행(메인 화면으로 전환)
        }
    }
}
