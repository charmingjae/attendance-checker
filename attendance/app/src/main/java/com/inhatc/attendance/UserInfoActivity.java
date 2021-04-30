package com.inhatc.attendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;


public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener{

    TextView studentNum;
    TextView txtAttendWhether;
    TextView txtAttendTime;
    Button btnDoLogout;
    Button btnTestAttendance;
    Button btnGoAdminMenu;
    
    FirebaseAuth firebaseAuth;
    String strStuNo;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        studentNum =(TextView) findViewById(R.id.studentNum);
        txtAttendWhether = (TextView) findViewById(R.id.txtAttendWhether);
        txtAttendTime = (TextView) findViewById(R.id.txtAttendTime);
        btnDoLogout = (Button) findViewById(R.id.btnDoLogout);
        btnTestAttendance = (Button) findViewById(R.id.btnTestAttendance);
        btnGoAdminMenu = (Button) findViewById(R.id.btnGoAdminMenu);
        btnDoLogout.setOnClickListener(this);
        btnTestAttendance.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

        Log.i("UserActivity", firebaseAuth.getCurrentUser().getEmail());
        strStuNo = firebaseAuth.getCurrentUser().getEmail().substring(0,9);

        studentNum.setText(strStuNo);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        readUser();

        String getStudentNum = studentNum.getText().toString();

        Log.i("TAG", getStudentNum);

    }


    private void writeNewUser(String userId, String userNumber, String userAttendance, String userAttendTime, String userPosition) {
        String getAttend = txtAttendWhether.getText().toString();
        if(getAttend.equals("O")){
            Toast.makeText(UserInfoActivity.this, "이미 출석 완료 상태입니다.", Toast.LENGTH_SHORT).show();
        }
        else{
            User user = new User(userNumber, userAttendance, userAttendTime, userPosition);

            mDatabase.child("users").child(userId).setValue(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Write was successful!
                            Toast.makeText(UserInfoActivity.this, "출석 완료", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Write failed
                            Toast.makeText(UserInfoActivity.this, "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    private void readUser(){
        String strStudentNum = firebaseAuth.getCurrentUser().getEmail().substring(0,9);
        mDatabase.child("users").child(strStudentNum).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if(dataSnapshot.getValue(User.class) != null){
                    User post = dataSnapshot.getValue(User.class);
                    Log.w("FireBaseData", "getData : " + post.getUserAttendance());

                    if (post.getUserPosition() != null) {

                        if(post.getUserPosition().equals("professor")){
                            studentNum.setTextColor(Color.BLUE);

                            txtAttendTime.setVisibility(View.GONE);
                            txtAttendWhether.setVisibility(View.GONE);
//            btnDoLogout.setVisibility(View.GONE);
                            btnTestAttendance.setVisibility(View.GONE);

                            btnGoAdminMenu.setVisibility(View.VISIBLE);
                        }
                        else{
                            txtAttendTime.setVisibility(View.VISIBLE);
                            txtAttendWhether.setVisibility(View.VISIBLE);
//            btnDoLogout.setVisibility(View.VISIBLE);
                            btnTestAttendance.setVisibility(View.VISIBLE);

                            btnGoAdminMenu.setVisibility(View.GONE);
                            if(post.getUserAttendance() != null){
                                txtAttendWhether.setText(post.getUserAttendance());
                                txtAttendTime.setText(post.getUserAttendTime());
                            }else{
                                txtAttendWhether.setText(' ');
                                txtAttendTime.setText(' ');
                            }
                        }
                    }

                } else {
                    Toast.makeText(UserInfoActivity.this, "데이터 없음...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("FireBaseData", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }


    @Override
    public void onClick(View view){
        if(view == btnDoLogout) {
            try {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return;
            }catch(Exception e){
                Log.i("Exception",e.getMessage());
                return;
            }
        }else if(view == btnTestAttendance){
            try{
                String getUserName = studentNum.getText().toString();
                SimpleDateFormat format1 = new SimpleDateFormat ( "MM-dd HH:mm:ss");
                Date time = new Date();
                String time1 = format1.format(time);

                if(getUserName.equals("professor")){
                    writeNewUser(getUserName,getUserName,"O", time1, "professor");
                }
                else{
                    writeNewUser(getUserName,getUserName,"O", time1, "student");
                }

            }catch(Exception e){
                Log.i("Exception", e.getMessage());
                return;
            }
        }
    }
}
