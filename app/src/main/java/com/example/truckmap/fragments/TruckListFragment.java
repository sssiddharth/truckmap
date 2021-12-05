package com.example.truckmap.fragments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.truckmap.R;
import com.example.truckmap.adapters.TruckListAdapter;
import com.example.truckmap.models.TruckListModelClass;
import com.example.truckmap.utils.MySingleton;
import com.example.truckmap.utils.Progress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TruckListFragment extends Fragment {
    View view;
    Progress progressDialog;
    List<TruckListModelClass> truckListModelClassList = new ArrayList<>();
    RecyclerView recyclerView;
    TruckListAdapter truckListAdapter;
    public TruckListFragment (){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.truck_list_frag, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        init();

        return view;
    }

    private void init() {
        progressDialog = new Progress(getActivity());
        if (progressDialog!=null) {
            progressDialog.show();
            progressDialog.setCancelable(false);
        }
        getTruckListData();
    }

    //GET TRUCK LIST DATA FROM API
    private void getTruckListData() {
            StringRequest str = new StringRequest(Request.Method.GET, "https://api.mystral.in/tt/mobile/logistics/searchTrucks?auth-company=PCH&companyId=33&deactivated=false&key=g2qb5jvucg7j8skpu5q7ria0mu&q-expand=true&q-include=lastRunningState,lastWaypoint", new Response.Listener<String>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(String response) {
                    Log.e("REPONCE", "response login " + response);
                    if (progressDialog!=null){
                        progressDialog.dismiss();
                    }
                    JSONObject json = null;
                    try {
                       json = new JSONObject(response);
                       JSONObject jsonResponse = json.getJSONObject("responseCode");
                       String msg = jsonResponse.getString("message");
                       if (msg.equalsIgnoreCase("success")){
                           JSONArray jsonArray = json.getJSONArray("data");
                           for (int i=0;i<jsonArray.length();i++){
                               TruckListModelClass truckListModelClass = new TruckListModelClass();
                               JSONObject object = jsonArray.getJSONObject(i);
                               String truckNumber = object.getString("truckNumber");

                               JSONObject lastwayobject = object.getJSONObject("lastWaypoint");
                               String lat = lastwayobject.getString("lat");
                               String lng = lastwayobject.getString("lng");
                               String create_time = lastwayobject.getString("createTime");
                               String speed = lastwayobject.getString("speed");
                               String updated_time = lastwayobject.getString("updateTime");
                               String ignition_on = lastwayobject.getString("ignitionOn");

                               JSONObject lastRunningobject = object.getJSONObject("lastRunningState");
                               String stop_start_time = lastRunningobject.getString("stopStartTime");
                               String truck_running_state = lastRunningobject.getString("truckRunningState");

                               truckListModelClass.setTruck_number(truckNumber);
                               truckListModelClass.setLat(lat);
                               truckListModelClass.setLng(lng);
                               truckListModelClass.setCreate_time(create_time);
                               truckListModelClass.setSpeed(speed);
                               truckListModelClass.setUpdated_time(updated_time);
                               truckListModelClass.setIgnition_on(ignition_on);
                               truckListModelClass.setStop_start_time(stop_start_time);
                               truckListModelClass.setTruck_running_state(truck_running_state);
                               truckListModelClassList.add(truckListModelClass);

                           }
                           LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                           recyclerView.setLayoutManager(linearLayoutManager);
                           truckListAdapter = new TruckListAdapter(getActivity(),truckListModelClassList);
                           recyclerView.setAdapter(truckListAdapter);
                       }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (progressDialog!=null){
                            progressDialog.dismiss();
                        }
                    }

                }


            }, new Response.ErrorListener() {


                @Override
                public void onErrorResponse(VolleyError v) {
                    if (progressDialog!=null){
                        progressDialog.dismiss();
                    }
                }
            });


            str.setShouldCache(false);
            MySingleton.getInstance(getActivity()).addToRequestQueue(str);
        }

}
