package com.inhatc.attendance;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectBusActivity extends AppCompatActivity {

    // Variable
    ArrayList<BusData> busList;
    ListView busListView;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bus);

        this.getBusList();

        // Get ListView
        busListView = (ListView)findViewById(R.id.lstBus);
        myAdapter = new MyAdapter(this, busList);
        busListView.setAdapter(myAdapter);

    }

    @Override
    public void onBackPressed() {
        BeaconDetectActivity.beaconStartScan();
        super.onBackPressed();
    }

    public void getBusList(){
        busList = new ArrayList<BusData>();
        // Test Data
        busList.add(new BusData("511",""));
        busList.add(new BusData("1540",""));
        busList.add(new BusData("6111",""));
        busList.add(new BusData("514",""));
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
