package com.inhatc.attendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    EditText userEmail, userPassword, pwConfirm, userName, userPhone;
    Button doRegister;

    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userEmail =(EditText) findViewById(R.id.edtUserId);
        userPassword = (EditText)findViewById(R.id.edtPassword);
        pwConfirm = (EditText)findViewById(R.id.edtPasswordConfirm);
        userName = (EditText)findViewById(R.id.edtUserName);

        doRegister = (Button)findViewById(R.id.btnDoRegister);

        firebaseAuth = FirebaseAuth.getInstance();

        doRegister.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void doRegister(){
//        String email = userEmail.getText().toString().trim();
        String email = userEmail.getText().toString().concat("@itc.ac.kr").trim();
        String pwd = userPassword.getText().toString().trim();
        String strPasswordConfirm = pwConfirm.getText().toString().trim();
        String strUserName = userName.getText().toString().trim();

        if(pwd.equals(strPasswordConfirm)) {

            Customer customer = new Customer(email, strUserName);

            Log.v("RegisterActivity email", email);
            Log.v("RegisterActivity pwd", pwd);
            Log.v("RegisterActivity confi", strPasswordConfirm);
            Log.v("RegisterActivity usern", strUserName);


            //email과 password가 제대로 입력되어 있다면 계속 진행된다.

            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setMessage("등록중입니다. 기다려 주세요...");
            progressDialog.show();

            // Auth에 등록하기
            firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mDatabase.child("consumers").child(strUserName).setValue(customer)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Write was successful!
                                        Toast.makeText(RegisterActivity.this, "회원가입 완료", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        finish();
                                        return;
//                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Write failed
                                        Toast.makeText(RegisterActivity.this, "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
                    }else{
                        Toast.makeText(RegisterActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
            });
        }
        else{
            Toast.makeText(this, "비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public void onClick(View view){
        if(view == doRegister){
            doRegister();
        }
    }
}
