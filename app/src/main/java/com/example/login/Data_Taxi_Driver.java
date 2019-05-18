package com.example.login;

public class Data_Taxi_Driver {
    private String ID,PW,NUMBER,POINT;
    private boolean AUTH;
    public Data_Taxi_Driver(){ }

    public Data_Taxi_Driver(String ID,String PW,String NUMBER,String POINT,boolean AUTH){
        this.ID = ID;
        this.PW = PW;
        this.NUMBER = NUMBER;
        this.POINT = POINT;
        this.AUTH = AUTH;
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
    public boolean getAUTH(){ return AUTH;}

    public void setAUTH(boolean AUTH) {
        this.AUTH = AUTH;
    }
}
