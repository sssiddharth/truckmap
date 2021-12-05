package com.example.truckmap.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.truckmap.R;
import com.example.truckmap.models.TruckListModelClass;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TruckListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<TruckListModelClass> truckListModelClassList = new ArrayList<>();


    public TruckListAdapter(Context context, List<TruckListModelClass>truckListModelClassList) {
        this.context = context;
        this.truckListModelClassList = truckListModelClassList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = inflater.inflate(R.layout.truck_list_raw, parent, false);
        return new DataHolder(view);
    }

    DataHolder dataHolder;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        dataHolder = (DataHolder) holder;
        dataHolder.tv_truck_number.setText(truckListModelClassList.get(position).getTruck_number());
        dataHolder.tv_last_updated.setText(String.valueOf(convertMilisectoDays(Long.parseLong(truckListModelClassList.get(position).getCreate_time()))));

        if (truckListModelClassList.get(position).getTruck_running_state().equalsIgnoreCase("1")){
        dataHolder.tv_running_stop.setText("Running since last" + " "+String.valueOf(convertMilisectoDays(Long.parseLong(truckListModelClassList.get(position).getStop_start_time())))+ " "+"days");
        }else{
            dataHolder.tv_running_stop.setText("Stopped since last" + " "+String.valueOf(convertMilisectoDays(Long.parseLong(truckListModelClassList.get(position).getStop_start_time())))+ " "+"days");
        }
        dataHolder.tv_speed.setText(String.format("%.2f",Double.parseDouble(truckListModelClassList.get(position).getSpeed())));
    }

    private long convertMilisectoDays(long milliseconds) {
        final long dy = TimeUnit.MILLISECONDS.toDays(milliseconds);
        final long hr = TimeUnit.MILLISECONDS.toHours(milliseconds)
                - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(milliseconds));
        final long min = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds));
        final long sec = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds));
        return dy;
    }

    @Override
    public int getItemCount() {
        return truckListModelClassList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class DataHolder extends RecyclerView.ViewHolder {
    TextView tv_truck_number,tv_last_updated,tv_running_stop,tv_speed,tv_days;
        public DataHolder(View itemView) {
            super(itemView);
            tv_truck_number = (itemView).findViewById(R.id.truck_number);
            tv_last_updated = (itemView).findViewById(R.id.last_updated);
            tv_running_stop = (itemView).findViewById(R.id.running_stopped);
            tv_speed = (itemView).findViewById(R.id.speed);
            tv_days = (itemView).findViewById(R.id.days);
        }
    }
}

