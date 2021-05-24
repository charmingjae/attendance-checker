package com.inhatc.attendance;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReservationResultActivity extends AppCompatActivity {

    TextView txtBusNumber, txtRideStation, txtStopStation;


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

        txtBusNumber.setText(busNumber);
        txtRideStation.setText(rideStation);
        txtStopStation.setText(stopStation);


    }
}
