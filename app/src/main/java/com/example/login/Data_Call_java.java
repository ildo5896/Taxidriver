package com.example.login;

public class Data_Call_java {
    String name, phonenumber, time, arrive, arrive_latitude, arrive_longitude, start, start_latitude, start_longitude, distance, index, taxi_name, taxi_phonenumber, taxi_number;
    int person, pay;

    Data_Call_java() {
    }

    Data_Call_java(String name, String phonenumber, String index
            , String start, String start_latitude, String start_longitude
            , String arrive, String arrive_latitude, String arrive_longitude
            , String time, String distance, int person, int pay
            , String taxi_name, String taxi_phonenumber, String taxi_number) {
        this.name = name;
        this.phonenumber = phonenumber;
        this.index = index;
        this.start = start;
        this.start_latitude = start_latitude;
        this.start_longitude = start_longitude;
        this.arrive = arrive;
        this.arrive_latitude = arrive_latitude;
        this.arrive_longitude = arrive_longitude;
        this.time = time;
        this.distance = distance;
        this.person = person;
        this.pay = pay;
        this.taxi_name = taxi_name;
        this.taxi_phonenumber = taxi_phonenumber;
        this.taxi_number = taxi_number;
    }

    public int getPay() {
        return pay;
    }

    public int getPerson() {
        return person;
    }

    public String getArrive() {
        return arrive;
    }

    public String getArrive_latitude() {
        return arrive_latitude;
    }

    public String getArrive_longitude() {
        return arrive_longitude;
    }

    public String getDistance() {
        return distance;
    }

    public String getName() {
        return name;
    }

    public String getIndex() {
        return index;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getStart() {
        return start;
    }

    public String getStart_latitude() {
        return start_latitude;
    }

    public String getStart_longitude() {
        return start_longitude;
    }

    public String getTaxi_name() {
        return taxi_name;
    }

    public String getTaxi_number() {
        return taxi_number;
    }

    public String getTaxi_phonenumber() {
        return taxi_phonenumber;
    }

    public String getTime() {
        return time;
    }

    public void setArrive(String arrive) {
        this.arrive = arrive;
    }

    public void setArrive_latitude(String arrive_latitude) {
        this.arrive_latitude = arrive_latitude;
    }

    public void setArrive_longitude(String arrive_longitude) {
        this.arrive_longitude = arrive_longitude;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public void setPerson(int person) {
        this.person = person;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setStart_latitude(String start_latitude) {
        this.start_latitude = start_latitude;
    }

    public void setStart_longitude(String start_longitude) {
        this.start_longitude = start_longitude;
    }

    public void setTaxi_name(String taxi_name) {
        this.taxi_name = taxi_name;
    }

    public void setTaxi_number(String taxi_number) {
        this.taxi_number = taxi_number;
    }

    public void setTaxi_phonenumber(String taxi_phonenumber) {
        this.taxi_phonenumber = taxi_phonenumber;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
