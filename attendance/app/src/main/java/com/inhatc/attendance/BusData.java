package com.inhatc.attendance;

public class BusData {
    private String busNumber;
    private String busType;
    private String busId;

    public BusData(String busNumber, String busType, String busId){
        this.busNumber = busNumber;
        this.busType = busType;
        this.busId = busId;
    }

    public String getBusNumber()
    {
        return this.busNumber;
    }

    public String getBusType()
    {
        return this.busType;
    }

    public String getBusId() { return this.busId; }

    public void setBusNumber(String busNumber) { this.busNumber = busNumber; }

    public void setBusType(String busType) { this.busType = busType; }

    public void setBusId(String busId) { this.busId = busId; }


}
