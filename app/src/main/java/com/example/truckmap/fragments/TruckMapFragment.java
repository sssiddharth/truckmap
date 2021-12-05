package com.example.truckmap.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TruckMapFragment extends Fragment {

    View view;
    Progress progressDialog;
    List<TruckListModelClass> truckListModelClassList = new ArrayList<>();
    RecyclerView recyclerView;
    MapView mMapView;
    private GoogleMap googleMap;
    SupportMapFragment mapFragment;
    private LatLngBounds bounds;
    private LatLngBounds.Builder builder;
    public TruckMapFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.truck_map_frag, container, false);
        progressDialog = new Progress(getActivity());
        if (progressDialog!=null) {
            progressDialog.show();
            progressDialog.setCancelable(false);
        }
        getTruckListData();
         mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment


        return view;
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
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap mMap) {
                                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                builder = new LatLngBounds.Builder();
                                mMap.clear(); //clear old markers
                                for (int i = 0; i < truckListModelClassList.size(); i++) {
                                    // below lin is use to zoom our camera on map.
                              long hours = 14400000;
                              long total = Long.parseLong(truckListModelClassList.get(i).getCreate_time())-Long.parseLong(truckListModelClassList.get(i).getStop_start_time());

                                 if (total > hours){
                                     LatLng latLngForMarker = new LatLng(Double.parseDouble(truckListModelClassList.get(i).getLat()), Double.parseDouble(truckListModelClassList.get(i).getLng()));

                                     MarkerOptions markerOptions = new MarkerOptions();

                                     markerOptions.position(latLngForMarker).title(truckListModelClassList.get(i).getTruck_number()).icon(BitmapDescriptorFactory.fromResource(R.drawable.red_marker));
                                     mMap.addMarker(markerOptions);
                                     builder.include(markerOptions.getPosition());

                                 }
                                    else if (truckListModelClassList.get(i).getTruck_running_state().equalsIgnoreCase("1")){
                                        LatLng latLngForMarker = new LatLng(Double.parseDouble(truckListModelClassList.get(i).getLat()), Double.parseDouble(truckListModelClassList.get(i).getLng()));
                                     MarkerOptions markerOptions = new MarkerOptions();

                                     markerOptions.position(latLngForMarker).title(truckListModelClassList.get(i).getTruck_number()).icon(BitmapDescriptorFactory.fromResource(R.drawable.green_marker));
                                     mMap.addMarker(markerOptions);
                                     builder.include(markerOptions.getPosition());
                                    }else if(truckListModelClassList.get(i).getTruck_running_state().equalsIgnoreCase("0")&& truckListModelClassList.get(i).getIgnition_on().equalsIgnoreCase("false")){
                                        LatLng latLngForMarker = new LatLng(Double.parseDouble(truckListModelClassList.get(i).getLat()), Double.parseDouble(truckListModelClassList.get(i).getLng()));
                                     MarkerOptions markerOptions = new MarkerOptions();

                                     markerOptions.position(latLngForMarker).title(truckListModelClassList.get(i).getTruck_number()).icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_marker));
                                     mMap.addMarker(markerOptions);
                                     builder.include(markerOptions.getPosition());
                                    }else if(truckListModelClassList.get(i).getTruck_running_state().equalsIgnoreCase("0")&& truckListModelClassList.get(i).getIgnition_on().equalsIgnoreCase("true")){
                                        LatLng latLngForMarker = new LatLng(Double.parseDouble(truckListModelClassList.get(i).getLat()), Double.parseDouble(truckListModelClassList.get(i).getLng()));
                                     MarkerOptions markerOptions = new MarkerOptions();

                                     markerOptions.position(latLngForMarker).title(truckListModelClassList.get(i).getTruck_number()).icon(BitmapDescriptorFactory.fromResource(R.drawable.yellow_marker));
                                     mMap.addMarker(markerOptions);
                                     builder.include(markerOptions.getPosition());

                                    }
                                    bounds = builder.build();
                                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
                                    mMap.animateCamera(cu);
//                                    drawMarker(latLngForMarker,mMap);

                                }

                            }
                        });

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

    private void drawMarker(LatLng point, GoogleMap mMap) {

        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(point).title("");
        mMap.addMarker(markerOptions);
        builder.include(point);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(point));

    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }



}
