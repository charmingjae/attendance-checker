package com.inhatc.attendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;

//depencencies


import com.inhatc.attendance.BeaconListAdapter;
import com.minew.beaconset.BluetoothState;
import com.minew.beaconset.ConnectionState;
import com.minew.beaconset.MinewBeacon;
import com.minew.beaconset.MinewBeaconConnection;
import com.minew.beaconset.MinewBeaconConnectionListener;
import com.minew.beaconset.MinewBeaconManager;
import com.minew.beaconset.MinewBeaconManagerListener;
import com.inhatc.attendance.R;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.multidex.MultiDexApplication;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inhatc.attendance.R;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;



public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener{

    TextView studentNum;
    TextView txtAttendWhether;
    TextView txtAttendTime;
    Button btnDoLogout;
    Button btnTestAttendance;
    Button btnGoAdminMenu;
    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    String strStuNo;

    private DatabaseReference mDatabase;

    // Depencencies
    private RecyclerView mRecycle;
    private MinewBeaconManager mMinewBeaconManager;
    private BeaconListAdapter mAdapter;
    UserRssi comp = new UserRssi();
    private ProgressDialog mpDialog;
    public static MinewBeacon clickBeacon;
    private static final int REQUEST_ENABLE_BT = 2;
    private final int PERMISSION_COARSE_LOCATION = 122;
    // End

    String rssiValue = "";

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
        btnGoAdminMenu.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

        Log.i("UserActivity", firebaseAuth.getCurrentUser().getEmail());
        strStuNo = firebaseAuth.getCurrentUser().getEmail().substring(0,9);

        studentNum.setText(strStuNo);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        readUser();

        String getStudentNum = studentNum.getText().toString();

        Log.i("TAG", getStudentNum);

        initView();
        initManager();
        checkBluetooth();
        dialogshow();
        mMinewBeaconManager.startService();
        initPermission();

    }


    private void writeNewUser(String userId, String userNumber, String userAttendance, String userAttendTime, String userPosition) {
        String getAttend = txtAttendWhether.getText().toString();

        progressDialog = new ProgressDialog(UserInfoActivity.this);
        progressDialog.setMessage("출석중...");
        progressDialog.show();


        if(getAttend.equals("O")){
            Toast.makeText(UserInfoActivity.this, "이미 출석 완료 상태입니다.", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
        else{
            User user = new User(userNumber, userAttendance, userAttendTime, userPosition);

            mDatabase.child("users").child(userId).setValue(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Write was successful!
                            Toast.makeText(UserInfoActivity.this, "출석 완료", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Write failed
                            Toast.makeText(UserInfoActivity.this, "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
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

                // 1. RSSI 값 검사하기
                // 2. RSSI 값이 설정한 것 보다 낮으면 튕기기

                if(Integer.parseInt(rssiValue) < -60){
                    Toast.makeText(this, "신호 세기가 약합니다.", Toast.LENGTH_SHORT).show();
                }
                else{
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
                }

            }catch(Exception e){
                Log.i("Exception", e.getMessage());
                return;
            }
        }else if(view == btnGoAdminMenu){
            startActivity(new Intent(getApplicationContext(), RssiActivity.class));
            return;
        }
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        mRecycle = (RecyclerView) findViewById(R.id.main_recyeler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycle.setLayoutManager(layoutManager);
        mAdapter = new BeaconListAdapter();
        mRecycle.setAdapter(mAdapter);
        mRecycle.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager
                .HORIZONTAL));
    }

    private void initManager() {
        mMinewBeaconManager = MinewBeaconManager.getInstance(this);
    }

    /**
     * check Bluetooth state
     */
    private void checkBluetooth() {
        BluetoothState bluetoothState = mMinewBeaconManager.checkBluetoothState();
        switch (bluetoothState) {
            case BluetoothStateNotSupported:
                Toast.makeText(this, "Not Support BLE", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BluetoothStatePowerOff:
                showBLEDialog();
                break;
            case BluetoothStatePowerOn:
                break;
        }
    }

    private void initListener() {
        //scan listener;
        mMinewBeaconManager.setMinewbeaconManagerListener(new MinewBeaconManagerListener() {
            @Override
            public void onUpdateBluetoothState(BluetoothState state) {
                switch (state) {
                    case BluetoothStatePowerOff:
                        Toast.makeText(getApplicationContext(), "bluetooth off", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothStatePowerOn:
                        Toast.makeText(getApplicationContext(), "bluetooth on", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onRangeBeacons(List<MinewBeacon> beacons) {
                Collections.sort(beacons, comp);

                mAdapter.setData(beacons);
                Log.i("DATA1 ", mAdapter.getData(0).getName());
//                studentNum.setText(String.valueOf(mAdapter.getData(0).getRssi()));
                rssiValue = String.valueOf(mAdapter.getData(0).getRssi());
                Log.i("Rssi Value ", rssiValue);

            }

            @Override
            public void onAppearBeacons(List<MinewBeacon> beacons) {

                Log.i("DATA2 ", mAdapter.getData(0).getName());

            }

            @Override
            public void onDisappearBeacons(List<MinewBeacon> beacons) {

                Log.i("DATA3 ", mAdapter.getData(0).getName());

            }
        });

        mAdapter.setOnItemClickLitener(new BeaconListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                mpDialog.setMessage(getString(R.string.connecting)
                        + mAdapter.getData(position).getName());
                mpDialog.show();
                mMinewBeaconManager.stopScan();
                //connect to beacon
                MinewBeacon minewBeacon = mAdapter.getData(position);
                MinewBeaconConnection minewBeaconConnection = new MinewBeaconConnection(UserInfoActivity.this, minewBeacon);
                minewBeaconConnection.setMinewBeaconConnectionListener(minewBeaconConnectionListener);
                minewBeaconConnection.connect();
            }

            @Override
            public void onItemLongClick(View view, int position) {


                Log.i("DATA ", mAdapter.getData(position).getName());
            }
        });

    }

    private void initPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_COARSE_LOCATION);
        }else {
            Log.e("liu", "initPermission");
            mMinewBeaconManager.startScan();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_COARSE_LOCATION:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mMinewBeaconManager.startScan();
                    }
                }, 1000);

                break;
        }
    }

    //connect listener;
    MinewBeaconConnectionListener minewBeaconConnectionListener = new MinewBeaconConnectionListener() {
        @Override
        public void onChangeState(MinewBeaconConnection connection, ConnectionState state) {
            switch (state) {
                case BeaconStatus_Connected:
                    mpDialog.dismiss();
                    Intent intent = new Intent(UserInfoActivity.this, DetilActivity.class);
                    intent.putExtra("mac", connection.setting.getMacAddress());
                    startActivity(intent);
                    break;
                case BeaconStatus_ConnectFailed:
                case BeaconStatus_Disconnect:
                    if (mpDialog != null) {
                        mpDialog.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "连接断开", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    break;
            }
        }

        @Override
        public void onWriteSettings(MinewBeaconConnection connection, boolean success) {

        }
    };

    @Override
    protected void onResume() {
//        mMinewBeaconManager.startScan();
        initListener();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mMinewBeaconManager.stopScan();
        super.onPause();
    }

    protected void dialogshow() {
        mpDialog = new ProgressDialog(UserInfoActivity.this);
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

    private void showBLEDialog() {
        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                mMinewBeaconManager.startScan();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mMinewBeaconManager.stopService();
        super.onDestroy();
    }
}
