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

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SelectStopActivity extends AppCompatActivity {

    // Variable
    ArrayList<BusData> busList;
    ListView busListView;
    nodeAdapter nodeAdapter;
    TextView txtBusNumber;

    // Dialog
    private ProgressDialog mpDialog;

    // Thread
    private busNodeThread thread;
    // End

    public static String busId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_stop);

        // get Components
        txtBusNumber = (TextView)findViewById(R.id.txtBusNumber);
        String busNumber = getIntent().getStringExtra("busNum");
        busId = getIntent().getStringExtra("busId");
        String rideStation = getIntent().getStringExtra("rideStation");
        txtBusNumber.setText(busNumber);

        // Start Thread
        thread=new busNodeThread();
        thread.start();
        // End

        dialogshow();

        mpDialog.setMessage("해당 노선이 경유하는 정류장을 검색중입니다...");
        mpDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mpDialog.dismiss();

                // Get ListView
                busListView = (ListView)findViewById(R.id.lstBus);
                Log.d("TEST", busNodeThread.list_route.get(0));
                nodeAdapter = new nodeAdapter(getApplicationContext(), busNodeThread.list_route);
                busListView.setAdapter(nodeAdapter);

                busListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id){
                        // 버스 번호, 출발지 정보를 다음 인텐트로 넘기기
                        // 지금은 단순히 다음 인텐트로 넘어가게 했음

                        Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
                        intent.putExtra("busNum", busNumber);
                        intent.putExtra("rideStation", rideStation);
                        intent.putExtra("stopStation", nodeAdapter.getItem(position));
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        NetworkThread.list_busData.clear();
        busNodeThread.list_route.clear();
        BeaconDetectActivity.beaconStartScan();
        super.onBackPressed();
    }
//        myAdapter.notifyDataSetChanged();


        // * Samplecode how to insert bus data into busList
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//
//        Query query = reference.child("bus");
//        busList = new ArrayList<BusData>();
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    for(DataSnapshot issue : snapshot.getChildren()){
//                        String name = issue.child("name").getValue().toString();
//                        Log.i("RETURN VALUE NAME : ", name);
//                        busList.add(new BusData(name,""));
//                    }
//                }
//                // 됐다고 알림
//                myAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    protected void dialogshow() {
        mpDialog = new ProgressDialog(SelectStopActivity.this);
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
