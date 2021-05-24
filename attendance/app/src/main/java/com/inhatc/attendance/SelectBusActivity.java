package com.inhatc.attendance;

import android.content.Intent;
import android.os.Bundle;
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

    // Variable
    ArrayList<BusData> busList;
    ListView busListView;
    MyAdapter myAdapter;
    TextView txtRideStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bus);

        txtRideStop = (TextView)findViewById(R.id.txtRideStop);
        String rideStation = getIntent().getStringExtra("rideStation");
        txtRideStop.setText(rideStation);

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



                Intent intent = new Intent(getApplicationContext(), SelectStopActivity.class);
                intent.putExtra("busNum", myAdapter.getItem(position).getBusNumber());
                intent.putExtra("rideStation", rideStation);
                startActivity(intent);
                finish();
            }
        });

    }

    public void getBusList(){
        busList = new ArrayList<BusData>();
        // Test Data
        busList.add(new BusData("511",""));
        busList.add(new BusData("1540",""));
        busList.add(new BusData("6111",""));
        busList.add(new BusData("514",""));
    }
}
