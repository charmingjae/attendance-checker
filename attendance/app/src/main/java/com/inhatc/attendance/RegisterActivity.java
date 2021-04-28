package com.inhatc.attendance;

import android.app.ProgressDialog;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    EditText stuNo, stuPassword, pwConfirm;
    Button doRegister;

    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        stuNo =(EditText) findViewById(R.id.edtStuNo);
        stuPassword = (EditText)findViewById(R.id.edtPassword);
        pwConfirm = (EditText)findViewById(R.id.edtPasswordConfirm);

        doRegister = (Button)findViewById(R.id.btnDoRegister);

        firebaseAuth = FirebaseAuth.getInstance();

        doRegister.setOnClickListener(this);
    }

    private void doRegister(){
        String email = stuNo.getText().toString().trim();
        String pwd = stuPassword.getText().toString().trim();
        String strPasswordConfirm = pwConfirm.getText().toString().trim();

        if(pwd.equals(strPasswordConfirm)) {

            Log.v("RegisterActivity", email);
            Log.v("RegisterActivity", pwd);
            Log.v("RegisterActivity", strPasswordConfirm);

            //email과 password가 제대로 입력되어 있다면 계속 진행된다.

            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setMessage("등록중입니다. 기다려 주세요...");
            progressDialog.show();


            firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "회원가입 완료", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
