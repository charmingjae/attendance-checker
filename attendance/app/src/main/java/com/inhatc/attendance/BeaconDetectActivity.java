package com.inhatc.attendance;

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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.minew.beaconset.BluetoothState;
import com.minew.beaconset.ConnectionState;
import com.minew.beaconset.MinewBeacon;
import com.minew.beaconset.MinewBeaconConnection;
import com.minew.beaconset.MinewBeaconConnectionListener;
import com.minew.beaconset.MinewBeaconManager;
import com.minew.beaconset.MinewBeaconManagerListener;

import java.util.Collections;
import java.util.List;

public class BeaconDetectActivity extends AppCompatActivity {

    // Depencencies
    private RecyclerView mRecycle;
    private static MinewBeaconManager mMinewBeaconManager;
    private BeaconListAdapter mAdapter;
    UserRssi comp = new UserRssi();
    private ProgressDialog mpDialog;
    public static MinewBeacon clickBeacon;
    private static final int REQUEST_ENABLE_BT = 2;
    private final int PERMISSION_COARSE_LOCATION = 122;
    // End

    // Thread
    private NetworkThread thread;
    // End

    String rssiValue = "";
    String uuidValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_beacon);

        initView();
        initManager();
        checkBluetooth();
        dialogshow();
        mMinewBeaconManager.startService();
        initPermission();

        // Start Thread
//        thread=new NetworkThread();
//        thread.start();
        // End


        // 일단은 4초 후에 버스 선택 액티비티로 넘어가게 설정
        // 시나리오대로라면 비콘 탐지 후 그 비콘 아이디에 맞는 버스를 출력해야 된다.
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(getApplicationContext(), SelectBusActivity.class);
//                startActivity(intent);
//            }
//        }, 4000);
    }

    public static void beaconStartScan() {
        mMinewBeaconManager.startScan();
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

                try {
                    mAdapter.setData(beacons);
                    Log.i("DATA1 ", mAdapter.getData(0).getName());
//                studentNum.setText(String.valueOf(mAdapter.getData(0).getRssi()));
                    // 비콘의 신호 세기를 가져오는 부분
                    // getData(index) : 비콘이 여러개일 경우 인덱스로 특정 비콘의 정보를 가져온다
                    // getRssi() : 비콘의 라이브러리로 rssi 값을 가져옴
                    rssiValue = String.valueOf(mAdapter.getData(0).getRssi());
                    uuidValue = String.valueOf(mAdapter.getData(0).getUuid());
                    Log.i("Rssi Value ", rssiValue);
                    Log.i("UUID Value ", uuidValue);
                } catch(Exception e) {
                    Log.e("Range Exception : ", "잡힌거 없음");
                }

            }

            @Override
            public void onAppearBeacons(List<MinewBeacon> beacons) {
                try {
                    Log.i("DATA2 ", mAdapter.getData(0).getName());
                } catch (Exception e) {
                    Log.e("Appear Exception : ", "잡힌거 없음");
                }

            }

            @Override
            public void onDisappearBeacons(List<MinewBeacon> beacons) {
                try {
                    Log.i("DATA3 ", mAdapter.getData(0).getName());
                } catch (Exception e) {
                    Log.e("Disappear Exception : ", "잡힌거 없음");
                }

            }
        });

        mAdapter.setOnItemClickLitener(new BeaconListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
//                mpDialog.setMessage(getString(R.string.connecting)
//                        + mAdapter.getData(position).getName());
                mpDialog.setMessage("선택하신 정류장에 연결중입니다...");
                mpDialog.show();
                mMinewBeaconManager.stopScan();
                //connect to beacon
                MinewBeacon minewBeacon = mAdapter.getData(position);
                uuidValue = String.valueOf(mAdapter.getData(position).getUuid());
                Toast.makeText(getApplicationContext(), uuidValue, Toast.LENGTH_LONG).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), SelectBusActivity.class);
                        String slicing_uuid = uuidValue.substring(0, 8) + uuidValue.substring(9, 10);
                        intent.putExtra("busStopId", slicing_uuid);
                        startActivity(intent);
                        mpDialog.dismiss();
                    }
                }, 1000);

//                MinewBeaconConnection minewBeaconConnection = new MinewBeaconConnection(BeaconDetectActivity.this, minewBeacon);
//                minewBeaconConnection.setMinewBeaconConnectionListener(minewBeaconConnectionListener);
//                minewBeaconConnection.connect();
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
                    Intent intent = new Intent(BeaconDetectActivity.this, DetilActivity.class);
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
        mpDialog = new ProgressDialog(BeaconDetectActivity.this);
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
