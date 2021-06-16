package com.inhatc.attendance;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.minew.beaconset.MinewBeacon;
import com.inhatc.attendance.R;

import java.util.HashMap;
import java.util.List;


public class BeaconListAdapter extends RecyclerView.Adapter<com.inhatc.attendance.BeaconListAdapter.MyViewHolder> {

    private List<MinewBeacon> mMinewBeacons;

    boolean isFinded = false;

    private String bStop_name;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public String getBStop_name() { return this.bStop_name; }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.main_item, null);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.setDataAndUi(mMinewBeacons.get(position));

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mMinewBeacons != null) {
            return mMinewBeacons.size();
        }
        return 0;
    }

    public void setData(List<MinewBeacon> minewBeacons) {
        this.mMinewBeacons = minewBeacons;
        notifyDataSetChanged();
    }

    public MinewBeacon getData(int position) {
        try {
            return mMinewBeacons.get(position);
        } catch (Exception e) {
            Log.e("Exception : ", String.valueOf(e));
            return null;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private MinewBeacon mMinewBeacon;
        private final TextView mDevice_name;
//        private final TextView mDevice_uuid;
//        private final TextView mDevice_other;
//        private final TextView mConnectable;
//        private final TextView mdevice_status;


        public MyViewHolder(View itemView) {
            super(itemView);
            mDevice_name = (TextView) itemView.findViewById(R.id.device_name);
//            mDevice_uuid = (TextView) itemView.findViewById(R.id.device_uuid);
//            mDevice_other = (TextView) itemView.findViewById(R.id.device_other);
//            mConnectable = (TextView) itemView.findViewById(R.id.device_connectable);
//            mdevice_status = (TextView) itemView.findViewById(R.id.device_status);

        }

        public void setDataAndUi(MinewBeacon minewBeacon) {
            mMinewBeacon = minewBeacon;
            isFinded = false;

            // 정류장 리스트에서 uuid가 같은 값 찾은 뒤 해당 정류장 이름으로 setText
            for(int i=0; i<MainActivity.bStop_list.size(); i++) {
                if(MainActivity.bStop_list.get(i).getBStop_Uuid().equals(mMinewBeacon.getUuid())) {
                    bStop_name = MainActivity.bStop_list.get(i).getBStop_Name();
                    mDevice_name.setText(bStop_name);
                    isFinded = true;
                    break;
                }
            }

            if(isFinded) {

            } else {
                mDevice_name.setText(mMinewBeacon.getName());
            }
//            mDevice_uuid.setText("UUID:" + mMinewBeacon.getUuid());
//            if (mMinewBeacon.isConnectable()) {
//                mConnectable.setText("CONN: YES");
//            } else {
//                mConnectable.setText("CONN: NO");
//            }
            String format = String.format("Major:%s Minor:%s Rssi:%s Battery:%s",
                    mMinewBeacon.getMajor(),
                    mMinewBeacon.getMinor(),
                    mMinewBeacon.getRssi(),
                    mMinewBeacon.getBattery());
//            mDevice_other.setText(format);

            int getRssi = Integer.parseInt(String.valueOf(mMinewBeacon.getRssi()));
            String rssiStatus = "";

            if(getRssi < -50) {
                rssiStatus = "신호 약함";
            }
            else{
                rssiStatus = "양호";
            }

//            mdevice_status.setText(rssiStatus);

        }
    }

}
