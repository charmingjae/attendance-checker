package com.inhatc.attendance;

public class BusData {
    private String busNumber;
    private String busType;

    public BusData(String busNumber, String busType){
        this.busNumber = busNumber;
        this.busType = busType;
    }


    public String getBusNumber()
    {
        return this.busNumber;
    }

    public String getBusType()
    {
        return this.busType;
    }

}
