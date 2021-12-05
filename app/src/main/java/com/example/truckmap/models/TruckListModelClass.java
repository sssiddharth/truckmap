package com.example.truckmap.models;

public class TruckListModelClass {
    String truck_number;
    String lat;
    String lng;
    String create_time;
    String speed;
    String updated_time;
    String ignition_on;
    String stop_start_time;
    String truck_running_state;

    public TruckListModelClass(){

    }

    public TruckListModelClass(String truck_number,String lat,String lng,String create_time,String speed,String updated_time,String ignition_on,String stop_start_time,String truck_running_state){
        this.truck_number = truck_number;
        this.lat = lat;
        this.lng = lng;
        this.create_time = create_time;
        this.speed = speed;
        this.updated_time = updated_time;
        this.ignition_on = ignition_on;
        this.stop_start_time = stop_start_time;
        this.truck_running_state = truck_running_state;
    }

    public String getTruck_number() {
        return truck_number;
    }

    public void setTruck_number(String truck_number) {
        this.truck_number = truck_number;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(String updated_time) {
        this.updated_time = updated_time;
    }

    public String getIgnition_on() {
        return ignition_on;
    }

    public void setIgnition_on(String ignition_on) {
        this.ignition_on = ignition_on;
    }

    public String getStop_start_time() {
        return stop_start_time;
    }

    public void setStop_start_time(String stop_start_time) {
        this.stop_start_time = stop_start_time;
    }

    public String getTruck_running_state() {
        return truck_running_state;
    }

    public void setTruck_running_state(String truck_running_state) {
        this.truck_running_state = truck_running_state;
    }
}
