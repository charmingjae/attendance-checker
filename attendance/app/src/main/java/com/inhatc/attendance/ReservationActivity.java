package com.inhatc.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;

public class ReservationActivity extends AppCompatActivity implements View.OnClickListener{

    // Variable
    TextView txtBus, txtRideStation, txtStopStation;
    Button btnDoReservation;
    String busNumber, rideStation, stopStation, userPhone;
    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);


        txtBus = (TextView)findViewById(R.id.txtBus);
        txtRideStation = (TextView)findViewById(R.id.txtDepart);
        txtStopStation = (TextView)findViewById(R.id.txtArrive);
        btnDoReservation = (Button)findViewById(R.id.btnDoReservation);
        btnDoReservation.setOnClickListener(this);

        busNumber = getIntent().getStringExtra("busNum");
        rideStation = getIntent().getStringExtra("rideStation");
        stopStation = getIntent().getStringExtra("stopStation");

        txtBus.setText(busNumber);
        txtRideStation.setText(rideStation);
        txtStopStation.setText(stopStation);

    }

    @Override
    public void onBackPressed() {
        NetworkThread.list_busData.clear();
        busNodeThread.list_route.clear();
        BeaconDetectActivity.beaconStartScan();
        super.onBackPressed();
    }

    private void doReservation(String busNumber_param, String rideStation_param, String stopStation_param){
        // 'res' 항목에 예약 정보를 넣은 뒤 인텐트 이동
        //상세정보로 이동하기 전 데이터베이스의 예약 데이터 저장
        //보낼 예약 데이터 중 사용자 정보
        userPhone = mAuth.getCurrentUser().getEmail();
        userPhone = userPhone.substring(0,11);
        HashMap result = new HashMap<>();
        result.put("end", stopStation);
        result.put("phone", userPhone);
        result.put("start", rideStation);
        result.put("status", "wait");

        // firebase 정의
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("res").push().setValue(result);

        // 인텐트 이동
        Intent intent = new Intent(ReservationActivity.this, ReservationResultActivity.class);
        intent.putExtra("busNum", busNumber_param);
        intent.putExtra("rideStation", rideStation_param);
        intent.putExtra("stopStation", stopStation_param);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view){
        if(view == btnDoReservation){
            doReservation(busNumber, rideStation, stopStation);
        }
    }
}
