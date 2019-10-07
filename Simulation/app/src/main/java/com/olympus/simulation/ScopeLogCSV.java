package com.olympus.simulation;

import android.support.annotation.Nullable;

import java.io.Serializable;

public class ScopeLogCSV implements Simulation_Manager.CSVable, Serializable {
    String model;
    String serialNum;
    String name;
    String timeIn;
    String timeOut;
    String timeDiff;
    String procedure;
    String station;
    String state;

    ScopeLogCSV() {
        model = "";
        serialNum = "";
        name = "";
        timeIn = "";
        timeOut = "";
        timeDiff = "";
        procedure = "";
        station = "";
        state = "";
    }

    ScopeLogCSV(String model, String serialNum, String name, String timeIn, String timeOut, String procedure, String station, String state) {
        this.model = model;
        this.serialNum = serialNum;
        this.name = name;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        setTimeDiff(timeIn, timeOut);
        this.procedure = procedure;
        this.station = station;
        this.state = state;
    }

    private void setTimeDiff(String timeIn, String timeOut) {
        this.timeDiff = Time.convertToString(Time.convertToInt(timeOut) - Time.convertToInt(timeIn));
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
        setTimeDiff(timeIn, timeOut);
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
        setTimeDiff(timeIn, timeOut);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof ScopeLogCSV) {
            return ((ScopeLogCSV) obj).serialNum.equals(this.serialNum);
        }
        return super.equals(obj);
    }

    @Override
    public String getCSV() {
        return model + ","
                + serialNum + ","
                + name + ","
                + timeIn + ","
                + timeOut + ","
                + timeDiff + ","
                + procedure + ","
                + station + ","
                + state;
    }

    @Override
    public String getCSVHeader() {
        return "Model,Serial Number,Name,Time In,Time Out,Time Diff,Procedure,Station Name,State";
    }
}
