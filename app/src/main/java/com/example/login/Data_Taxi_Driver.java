package com.example.login;

public class Data_Taxi_Driver {
    private String ID,PW,NUMBER,POINT,PHONENUMBER;
    private boolean AUTH,CALL;
    public Data_Taxi_Driver(){ }

    public Data_Taxi_Driver(String ID,String PW,String NUMBER,String POINT,String PHONENUMBER,boolean AUTH,boolean CALL){
        this.ID = ID;
        this.PW = PW;
        this.NUMBER = NUMBER;
        this.POINT = POINT;
        this.PHONENUMBER = PHONENUMBER;
        this.AUTH = AUTH;
        this.CALL = CALL;
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

    public String getPHONENUMBER() {
        return PHONENUMBER;
    }
    public boolean getCALL(){ return CALL;}
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

    public void setPHONENUMBER(String PHONENUMBER) {
        this.PHONENUMBER = PHONENUMBER;
    }

    public void setCALL(boolean CALL) {
        this.CALL = CALL;
    }
}
