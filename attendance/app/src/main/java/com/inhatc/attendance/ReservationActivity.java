package com.inhatc.attendance;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Iterator;

public class ReservationActivity extends AppCompatActivity implements View.OnClickListener{

    // Variable
    TextView txtBus, txtRideStation, txtStopStation;
    Button btnDoReservation;
    String busNumber, rideStation, stopStation, userPhone;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    // 중복 체크 Boolean
    Boolean check_phone = false;

    // Dialog
    private ProgressDialog mpDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        dialogshow();

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
        userPhone = mAuth.getInstance().getCurrentUser().getEmail();
        userPhone = userPhone.substring(0,11);
        HashMap result = new HashMap<>();
        result.put("busNum", busNumber);
        result.put("end", stopStation);
        result.put("phone", userPhone);
        result.put("start", rideStation);
        result.put("status", "wait");

        check_phone = false;


        // firebase 정의
        mDatabase = FirebaseDatabase.getInstance().getReference().child("res");

        // 중복체크
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> child = snapshot.getChildren().iterator();

                while(child.hasNext()) {
                    if(child.next().child("phone").getValue().equals(userPhone)) {
                        check_phone = true;
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mpDialog.setMessage("예약정보를 확인하는 중입니다...");
        mpDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mpDialog.dismiss();
                // firebase 데이터 삽입
                if(!check_phone) {
                    mDatabase.push().setValue(result);
                    // 인텐트 이동
                    Intent intent = new Intent(ReservationActivity.this, ReservationResultActivity.class);
                    intent.putExtra("busNum", busNumber_param);
                    intent.putExtra("rideStation", rideStation_param);
                    intent.putExtra("stopStation", stopStation_param);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ReservationActivity.this, "이미 예약하셨습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }, 500);
    }

    @Override
    public void onClick(View view){
        if(view == btnDoReservation){
            doReservation(busNumber, rideStation, stopStation);
        }
    }

    protected void dialogshow() {
        mpDialog = new ProgressDialog(ReservationActivity.this);
        mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mpDialog.setTitle(null);//
        mpDialog.setIcon(null);//
        mpDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
            }
        });
        mpDialog.setCancelable(true);//
        mpDialog.setCanceledOnTouchOutside(false);
    }
}
