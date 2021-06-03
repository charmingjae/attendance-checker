package com.inhatc.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class ReservationResultActivity extends AppCompatActivity {

    TextView txtBusNumber, txtRideStation, txtStopStation;
    Button buttonCancle;

    FirebaseDatabase firebase_Database = FirebaseDatabase.getInstance();
    String res_header = "res";
    DatabaseReference res_Reference = firebase_Database.getReference().child(res_header);

    FirebaseAuth mAuth;

    // User's Phone Number
    String userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_result);

        String busNumber = getIntent().getStringExtra("busNum");
        String rideStation = getIntent().getStringExtra("rideStation");
        String stopStation = getIntent().getStringExtra("stopStation");

        txtBusNumber = (TextView)findViewById(R.id.txtBus);
        txtRideStation = (TextView)findViewById(R.id.txtDepart);
        txtStopStation = (TextView)findViewById(R.id.txtArrive);

        buttonCancle = (Button)findViewById(R.id.btnCancelResv);

        txtBusNumber.setText(busNumber);
        txtRideStation.setText(rideStation);
        txtStopStation.setText(stopStation);

        // 취소하기 버튼 ClickListener
        buttonCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeData();
            }
        });


    }

    @Override
    public void onBackPressed() {
        NetworkThread.list_busData.clear();
        busNodeThread.list_route.clear();
//        BeaconDetectActivity.beaconStartScan();
        Intent intent = new Intent(ReservationResultActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void removeData(){
        userPhone = mAuth.getInstance().getCurrentUser().getEmail();
        userPhone = userPhone.substring(0,11);
//        Log.e("Log", phone);
//        start = data.getStart();
//        end = data.getEnd();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query query = ref.child("res").orderByChild("phone").equalTo(userPhone);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }
}
