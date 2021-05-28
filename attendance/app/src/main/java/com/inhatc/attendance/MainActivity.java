package com.inhatc.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    // busStop Firebase
    String header = "busStop";
    String beacon_name = "";    // 실제 정류장 이름 들어갈 변수

    FirebaseDatabase busStop_database = FirebaseDatabase.getInstance();
    DatabaseReference busStop_Reference = busStop_database.getReference().child(header);
    // End

    // Object List
    public static ArrayList<bStopData> bStop_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 레이아웃에서 객체 연결하기
        Button goLogin = (Button)findViewById(R.id.btnGoLogin);
        Button goRegister = (Button)findViewById(R.id.btnGoRegister);

        // 파이어베이스 auth object
        firebaseAuth = FirebaseAuth.getInstance();

        bStop_list = new ArrayList<bStopData>();


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

        // busStop Database Event Listener
        busStop_Reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("BeaconDetectActivity", "ChildEventListener - onChildAdded : " + dataSnapshot.getValue());

                // Create bStopData
                String bStop_id = dataSnapshot.child("bStop_id").getValue().toString();
                String bStop_name = dataSnapshot.child("bStop_name").getValue().toString();
                String bStop_uuid = dataSnapshot.child("bStop_uuid").getValue().toString();

                // Add bStopData to bStopData's List
                bStopData data = new bStopData(bStop_id, bStop_name, bStop_uuid);
                bStop_list.add(data);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("BeaconDetectActivity", "ChildEventListener - onChildChanged : " + s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("BeaconDetectActivity", "ChildEventListener - onChildRemoved : " + dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("BeaconDetectActivity", "ChildEventListener - onChildMoved" + s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("BeaconDetectActivity", "ChildEventListener - onCancelled" + databaseError.getMessage());
            }
        });
    }
}