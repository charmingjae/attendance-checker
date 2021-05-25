package com.inhatc.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class ReservationActivity extends AppCompatActivity implements View.OnClickListener{

    // Variable
    TextView txtBus, txtRideStation, txtStopStation;
    Button btnDoReservation;
    String busNumber, rideStation, stopStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);


        txtBus = (TextView)findViewById(R.id.txtBus);
        txtRideStation = (TextView)findViewById(R.id.txtDepart);
        txtStopStation = (TextView)findViewById(R.id.txtArrive);
        btnDoReservation = (Button)findViewById(R.id.btnDoReservation);
        btnDoReservation.setOnClickListener(this);

        busNumber = getIntent().getStringExtra("busNum");
        rideStation = getIntent().getStringExtra("rideStation");
        stopStation = getIntent().getStringExtra("stopStation");

        txtBus.setText(busNumber);
        txtRideStation.setText(rideStation);
        txtStopStation.setText(stopStation);

    }

    private void doReservation(String busNumber_param, String rideStation_param, String stopStation_param){
        Intent intent = new Intent(ReservationActivity.this, ReservationResultActivity.class);
        intent.putExtra("busNum", busNumber_param);
        intent.putExtra("rideStation", rideStation_param);
        intent.putExtra("stopStation", stopStation_param);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view){
        if(view == btnDoReservation){
            doReservation(busNumber, rideStation, stopStation);
        }
    }
}
