package com.inhatc.attendance;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SelectBusActivity extends AppCompatActivity {

    public static String busStopId;

    // Variable
    ArrayList<BusData> busList;
    ListView busListView;
    MyAdapter myAdapter;
    TextView txtRideStop;

    private ProgressDialog mpDialog;

    // Thread
    private NetworkThread thread;
    // End

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bus);

        Intent getIntent = getIntent();
        busStopId = getIntent.getStringExtra("busStopId");

        // Start Thread
        thread=new NetworkThread();
        thread.start();
        // End

        txtRideStop = (TextView)findViewById(R.id.txtRideStop);
        String rideStation = getIntent().getStringExtra("rideStation");
        txtRideStop.setText(rideStation);

        dialogshow();

        mpDialog.setMessage("선택하신 정류장을 경유하는 버스를 검색중입니다...");
        mpDialog.show();

        // NetworkThread에서 경유하는 버스 정보를 가져오기까지 기다리기 위해 일부러 사용했음
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mpDialog.dismiss();
                getBusList();

                // Get ListView
                busListView = (ListView)findViewById(R.id.lstBus);
                myAdapter = new MyAdapter(getApplicationContext(), busList);
                busListView.setAdapter(myAdapter);

                busListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id){
                        // 버스 번호, 출발지 정보를 다음 인텐트로 넘기기
                        // 지금은 단순히 다음 인텐트로 넘어가게 했음

                        Intent intent = new Intent(getApplicationContext(), SelectStopActivity.class);
                        intent.putExtra("busNum", myAdapter.getItem(position).getBusNumber());
                        intent.putExtra("rideStation", rideStation);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }, 2000);

//        // Get ListView
//        busListView = (ListView)findViewById(R.id.lstBus);
//        myAdapter = new MyAdapter(this, busList);
//        busListView.setAdapter(myAdapter);

//        busListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView parent, View v, int position, long id){
//                // 버스 번호, 출발지 정보를 다음 인텐트로 넘기기
//                // 지금은 단순히 다음 인텐트로 넘어가게 했음
//
//
//
//                Intent intent = new Intent(getApplicationContext(), SelectStopActivity.class);
//                intent.putExtra("busNum", myAdapter.getItem(position).getBusNumber());
//                intent.putExtra("rideStation", rideStation);
//                startActivity(intent);
//                finish();
//            }
//        });
    }

    @Override
    public void onBackPressed() {
//        busList.clear();
        NetworkThread.list_routeNo.clear();
        BeaconDetectActivity.beaconStartScan();
        super.onBackPressed();
    }

    public void getBusList(){
        busList = new ArrayList<BusData>();

        // Input Bus List
        for(int i=0; i<NetworkThread.list_routeNo.size(); i++) {
            busList.add(new BusData(NetworkThread.list_routeNo.get(i), ""));
        }
        Log.e("test", String.valueOf(busList));
    }

    protected void dialogshow() {
        mpDialog = new ProgressDialog(SelectBusActivity.this);
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
