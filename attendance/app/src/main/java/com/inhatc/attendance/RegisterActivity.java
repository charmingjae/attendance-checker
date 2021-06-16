package com.inhatc.attendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    EditText userEmail, userPassword, pwConfirm, userName, userPhone, edtPhoneValid, driverBusNum;
    Button doRegister, btnPhoneValid, btnChkCode;
    RadioGroup userPosition;
    String selected_position;

    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    private DatabaseReference mDatabase;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    PhoneAuthCredential credential;
    String smsCode = "";
    @NonNull String mVerificationId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userEmail =(EditText) findViewById(R.id.edtUserId);
        userPassword = (EditText)findViewById(R.id.edtPassword);
        pwConfirm = (EditText)findViewById(R.id.edtPasswordConfirm);
        userName = (EditText)findViewById(R.id.edtUserName);
        edtPhoneValid = (EditText)findViewById(R.id.edtPhoneValid);
        driverBusNum = (EditText)findViewById(R.id.edtBusNum);

        doRegister = (Button)findViewById(R.id.btnDoRegister);
        btnPhoneValid = (Button)findViewById(R.id.btnPhoneValid);
        btnChkCode = (Button)findViewById(R.id.btnChkCode);

        userPosition = (RadioGroup)findViewById(R.id.positionGroup);
        userPosition.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        firebaseAuth = FirebaseAuth.getInstance();

        doRegister.setOnClickListener(this);
        btnPhoneValid.setOnClickListener(this);
        btnChkCode.setOnClickListener(this);

        driverBusNum.setVisibility(View.GONE);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if(i == R.id.customer) {
                driverBusNum.setVisibility(View.GONE);
                selected_position = "customer";
            } else if(i == R.id.driver) {
                driverBusNum.setVisibility(View.VISIBLE);
                selected_position = "driver";
            }
        }
    };

    private void doRegister(){
//        String email = userEmail.getText().toString().trim();
        String email = userEmail.getText().toString().concat("@itc.ac.kr").trim();
        String pwd = userPassword.getText().toString().trim();
        String strPasswordConfirm = pwConfirm.getText().toString().trim();
        String strUserName = userName.getText().toString().trim();

        if(pwd.equals(strPasswordConfirm)) {
            Customer customer;
            if(selected_position.equals("customer")) {
                customer = new Customer(email, strUserName, selected_position);
            } else {
                customer = new Customer(email, strUserName, selected_position, driverBusNum.getText().toString());
            }

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
                                        startActivity(new Intent(getApplicationContext(), LoadingActivity.class));
                                        return;
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

    // 휴대전화 입력 후 버튼 눌렀을 때
    public void doGetNumber(){
        firebaseAuth.setLanguageCode("ko");
        String phoneNumber = userEmail.getText().toString();
        // Ex) 01012345678 -> +82 10-1234-5678
        String converted_number = "+82 " + phoneNumber.substring(1, 3) + "-" + phoneNumber.substring(3, 7) +
                                    "-" + phoneNumber.substring(7, 11);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(converted_number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential credential) {
                                Log.d("Log", "onVerificationCompleted:" + credential);

                                signInWithPhoneAuthCredential(credential);
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                Log.w("Log", "onVerificationFailed", e);

                                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                    // Invalid request
                                    Log.e("Error!!!", "무슨 에러냐 이건?");
                                    Toast.makeText(RegisterActivity.this, "예기치 못한 오류가 발생하였습니다. 고객센터로 문의주시기 바랍니다.", Toast.LENGTH_SHORT);
                                } else if (e instanceof FirebaseTooManyRequestsException) {
                                    // The SMS quota for the project has been exceeded
                                    Log.e("Error!!!", "요청이 너무 많다는데");
                                    Toast.makeText(RegisterActivity.this, "요청이 너무 많습니다. 잠시 후 이용해 주세요.", Toast.LENGTH_SHORT);
                                }

                                // Show a message and update the UI
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                Log.d("Log", "onCodeSent:" + verificationId);

                                // Save verification ID and resending token so we can use them later
                                mVerificationId = verificationId;
                                @NonNull PhoneAuthProvider.ForceResendingToken mResendToken = token;
                            }
                        }
)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

        return;
    }

    // 인증번호 입력 후 인증번호 확인용
    public void doValidCode(){
        // 비활성화 되어 있는 회원가입 버튼 활성화
        // if문으로 인증번호가 확인 되었을 때 활성화 시켜야 됨
        credential = PhoneAuthProvider.getCredential(mVerificationId, edtPhoneValid.getText().toString());
        signInWithPhoneAuthCredential(credential);
        return;
    }

    @Override
    public void onClick(View view){
        if(view == doRegister){
            doRegister();
        }
        else if(view == btnPhoneValid){
            doGetNumber();
            return;
        }
        else if(view == btnChkCode){
            // 인증번호 입력 후 인증번호 확인
            Log.e("TEST", "TEST");
            doValidCode();
            return;
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(RegisterActivity.this, "인증되었습니다.", Toast.LENGTH_SHORT).show();
                        edtPhoneValid.setText(credential.getSmsCode());
                        doRegister.setEnabled(true);
                        firebaseAuth.signOut();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "인증번호를 확인해 주시기 바랍니다..", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
