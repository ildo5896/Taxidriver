package com.example.login;

public class Data_Taxi_Driver {
    private String ID,PW,NUMBER,POINT;
    public Data_Taxi_Driver(){ }

    public Data_Taxi_Driver(String ID,String PW,String NUMBER,String POINT){
        this.ID = ID;
        this.PW = PW;
        this.NUMBER = NUMBER;
        this.POINT = POINT;
    }

    public String getID() {
        return ID;
    }

    public String getNUMBER() {
        return NUMBER;
    }

    public String getPOINT() {
        return POINT;
    }

    public String getPW() {
        return PW;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setNUMBER(String NUMBER) {
        this.NUMBER = NUMBER;
    }

    public void setPOINT(String POINT) {
        this.POINT = POINT;
    }

    public void setPW(String PW) {
        this.PW = PW;
    }
}
