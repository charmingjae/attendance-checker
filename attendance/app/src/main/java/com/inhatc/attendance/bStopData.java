package com.inhatc.attendance;

public class bStopData {
    private String bStop_id;
    private String bStop_name;
    private String bStop_uuid;

    public bStopData(String bStop_id, String bStop_name, String bStop_uuid){
        this.bStop_id = bStop_id;
        this.bStop_name = bStop_name;
        this.bStop_uuid = bStop_uuid;
    }


    public String getBStop_Id()
    {
        return this.bStop_id;
    }

    public String getBStop_Name()
    {
        return this.bStop_name;
    }

    public String getBStop_Uuid() { return this.bStop_uuid; }
}
