package com.inhatc.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class BeaconDetectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_beacon);


        // 일단은 4초 후에 버스 선택 액티비티로 넘어가게 설정
        // 시나리오대로라면 비콘 탐지 후 그 비콘 아이디에 맞는 버스를 출력해야 된다.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), SelectBusActivity.class);
                startActivity(intent);
            }
        }, 4000);



    }


}
