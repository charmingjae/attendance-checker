package com.inhatc.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 레이아웃에서 객체 연결하기
        Button goLogin = (Button)findViewById(R.id.btnGoLogin);
        Button goRegister = (Button)findViewById(R.id.btnGoRegister);

        // 파이어베이스 auth object
        firebaseAuth = FirebaseAuth.getInstance();


        // 메인 화면에서 '로그인' 버튼 눌렀을 때 로그인 화면으로 이동하기
        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        // '회원가입' 버튼 클릭 리스너
        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

//        if(firebaseAuth.getCurrentUser() != null){
//            // 이미 로그인 되었다면 이 액티비티를 종료함
//            finish();
//            // 그리고 UserInfo 액티비티를 연다.
//            startActivity(new Intent(getApplicationContext(), UserInfoActivity.class));
//        }
    }
}