package com.inhatc.attendance;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    // busStop Firebase
    String busStop_header = "busStop";
    String beacon_name = "";    // 실제 정류장 이름 들어갈 변수

    FirebaseDatabase firebase_Database = FirebaseDatabase.getInstance();
    DatabaseReference busStop_Reference = firebase_Database.getReference().child(busStop_header);
    // End

    // Res Info Firebase
    String res_header = "res";
    DatabaseReference res_Reference = firebase_Database.getReference().child(res_header);

    // Firebase Auth
    FirebaseAuth mAuth;

    // User Phone Number
    String userPhone;

    // User Reservation Info
    String start, end, busNumber, phone;

    // check Reservation Info
    Boolean check_phone;

    // Object List
    public static ArrayList<bStopData> bStop_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        check_phone = false;

        // 레이아웃에서 객체 연결하기
        Button goLogin = (Button)findViewById(R.id.btnGoLogin);
        Button goRegister = (Button)findViewById(R.id.btnGoRegister);

        // 파이어베이스 auth object
        firebaseAuth = FirebaseAuth.getInstance();

        try {
            userPhone = mAuth.getInstance().getCurrentUser().getEmail();
            userPhone = userPhone.substring(0, 11);
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        bStop_list = new ArrayList<bStopData>();


        // 메인 화면에서 '로그인' 버튼 눌렀을 때 로그인 화면으로 이동하기
        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check_phone) {
                    Intent intent = new Intent(getApplicationContext(), ReservationResultActivity.class);
                    intent.putExtra("rideStation", start);
                    intent.putExtra("stopStation", end);
                    intent.putExtra("busNum", busNumber);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
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

        // res Database Listener
        res_Reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> child = snapshot.getChildren().iterator();
                start = "";
                end = "";
                phone = "";
                busNumber = "";
                check_phone = false;
                Log.e("Changed!!!", "Changed!!!");

                while(child.hasNext()) {
                    String string_data = child.next().getValue().toString();
                    String slicing_data[] = string_data.split(",");
                    phone = slicing_data[0].substring(7, slicing_data[0].lastIndexOf(""));

                    if(phone.equals(userPhone)) {
                        // Data preprocessing
//                        busNumber = slicing_data[1].replaceAll(" ", "");
//                        busNumber = slicing_data[1].replaceAll("=", "");
//                        start = slicing_data[2].replaceAll(" ", "");
//                        start = slicing_data[2].replaceAll("=", "");
//                        end = slicing_data[3].replaceAll(" ", "");
//                        end = slicing_data[3].replaceAll("=", "");
//
//                        busNumber = busNumber.replaceAll("busNum", "");
//                        start = start.replaceAll("start", "");
//                        end = end.replaceAll("end", "");
                        busNumber = slicing_data[1].substring(8, slicing_data[1].lastIndexOf(""));
                        start = slicing_data[2].substring(7, slicing_data[2].lastIndexOf(""));
                        end = slicing_data[3].substring(5, slicing_data[3].lastIndexOf(""));

                        check_phone = true;
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}