package com.inhatc.attendance;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

// 버스 노선 Adapter [ 해당 노선이 경유하는 정류장 목록 ListView Adapter ]
public class nodeAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<String> sample;

    public nodeAdapter(Context context, ArrayList<String> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public String getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.layout_listview, null);

        TextView busNumber = (TextView)view.findViewById(R.id.busNumber);
        TextView grade = (TextView)view.findViewById(R.id.grade);


        // setText
        busNumber.setText(sample.get(position));
        grade.setText(sample.get(position));
//        if(movieName.getText().toString().equals("9200")){
//            movieName.setTextColor(Color.parseColor("#FF0000"));
//        }

        return view;
    }
}