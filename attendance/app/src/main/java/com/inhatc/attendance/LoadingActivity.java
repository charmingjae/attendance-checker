package com.inhatc.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.os.AsyncTask;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class LoadingActivity extends AppCompatActivity {
    private ProgressDialog mpDialog;

    FirebaseAuth firebaseAuth;

    String userPhone;
    String userPosition;
    String driverBusNum;

    // User info Firebase
    FirebaseDatabase firebase_Database = FirebaseDatabase.getInstance();
    String user_header = "consumers";
    DatabaseReference user_Reference = firebase_Database.getReference().child(user_header);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        firebaseAuth = FirebaseAuth.getInstance();

        dialogshow();

        mpDialog.setMessage("메인화면을 불러오는 중입니다...");
        mpDialog.show();

        // user Database Listener
        user_Reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (firebaseAuth.getCurrentUser() != null) {
                    // 사용자의 전화번호를 가져옴
                    userPhone = firebaseAuth.getCurrentUser().getEmail();
                    userPhone = userPhone.substring(0, 11);
                    if(snapshot.exists()){
                        for(DataSnapshot issue : snapshot.getChildren()){
                            String userId = issue.child("userId").getValue().toString().substring(0, 11);
                            if(userPhone.equals(userId)) {
                                userPosition = issue.child("userPosition").getValue().toString();
                                if(userPosition.equals("driver")) {
                                    driverBusNum = issue.child("busNum").getValue().toString();
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.putExtra("userPhone", userPhone);
//                intent.putExtra("userPosition", userPosition);
                MainActivity.userPhone = userPhone;
                MainActivity.userPosition = userPosition;
                try {
                    if (userPosition.equals("driver")) {
                        MainActivity.driverBusNum = driverBusNum;
                    }
                } catch (Exception e) {
                    Log.e("Exception", e.toString());
                }
                startActivity(intent);
                mpDialog.dismiss();
            }
        }, 2000);
    }

    protected void dialogshow() {
        mpDialog = new ProgressDialog(LoadingActivity.this);
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