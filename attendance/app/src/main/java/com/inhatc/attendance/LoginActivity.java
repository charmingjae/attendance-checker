package com.inhatc.attendance;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    EditText edtLoginNUM;
    EditText edtLoginPW;
    Button btnDoLogin;
    ProgressDialog progressDialog;
    //define firebase object
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        edtLoginNUM = (EditText)findViewById(R.id.edtLoginNUM);
        edtLoginPW = (EditText)findViewById(R.id.edtLoginPW);
        btnDoLogin = (Button)findViewById(R.id.btnDoLogin);
        btnDoLogin.setOnClickListener(this);


        //initializig firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            // How to get logined user email
            Log.i("LoginActivity",firebaseAuth.getCurrentUser().getEmail());
            // 이미 로그인 되었다면 이 액티비티를 종료함
            finish();
            // 그리고 UserInfo 액티비티를 연다.
            startActivity(new Intent(getApplicationContext(), UserInfoActivity.class));
        }
    }

    //firebase userLogin method
    private void userLogin(){
        String email = edtLoginNUM.getText().toString().trim();
        String password = edtLoginPW.getText().toString().trim();

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("로그인중입니다. 잠시 기다려 주세요...");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            finish();
                            progressDialog.dismiss();
                            startActivity(new Intent(getApplicationContext(), UserInfoActivity.class));
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "로그인 실패!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view == btnDoLogin) {
            userLogin();
        }
    }

}
