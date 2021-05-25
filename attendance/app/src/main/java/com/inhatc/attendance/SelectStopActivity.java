package com.inhatc.attendance;

import android.content.Intent;
import android.os.Bundle;
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
    MyAdapter myAdapter;
    TextView txtBusNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_stop);

        // get Components
        txtBusNumber = (TextView)findViewById(R.id.txtBusNumber);
        String busNumber = getIntent().getStringExtra("busNum");
        String rideStation = getIntent().getStringExtra("rideStation");
        txtBusNumber.setText(busNumber);


        this.getBusList();

        // Get ListView
        busListView = (ListView)findViewById(R.id.lstBus);
        myAdapter = new MyAdapter(this, busList);
        busListView.setAdapter(myAdapter);

        busListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                // 버스 번호, 출발지 정보를 다음 인텐트로 넘기기
                // 지금은 단순히 다음 인텐트로 넘어가게 했음

                Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
                intent.putExtra("busNum", busNumber);
                intent.putExtra("rideStation", rideStation);
                intent.putExtra("stopStation", myAdapter.getItem(position).getBusNumber());
                startActivity(intent);
                finish();
            }
        });

    }

    public void getBusList(){
        busList = new ArrayList<BusData>();
        // Test Data
        busList.add(new BusData("주안역 환승 정류장",""));
        busList.add(new BusData("SK SKY VIEW",""));
        busList.add(new BusData("학산 소극장",""));
        busList.add(new BusData("정석항공고등학교",""));
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
    }
}
