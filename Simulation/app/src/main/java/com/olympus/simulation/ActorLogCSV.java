package com.olympus.simulation;

import android.support.annotation.Nullable;

import java.io.Serializable;

public class ActorLogCSV implements Simulation_Manager.CSVable, Serializable {
    String type;
    String id;
    String name;
    String timeIn;
    String timeOut;
    String timeDiff;
    String procedure;
    String station;
    String state;

    ActorLogCSV() {
        type = "";
        id = "";
        name = "";
        timeIn = "";
        timeOut = "";
        timeDiff = "";
        procedure = "";
        station = "";
        state = "";
    }

    ActorLogCSV(String type, String id, String name, String timeIn, String timeOut, String procedure, String station, String state) {
        this.type = type;
        this.id = id;
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

    public void setType(String type) {
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
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
        if(obj instanceof ActorLogCSV) {
            System.out.println(((ActorLogCSV) obj).name + "==" + this.name);
            return ((ActorLogCSV) obj).name.equals(this.name);
        }
        return super.equals(obj);
    }

    @Override
    public String getCSV() {
        return type + ","
                + id + ","
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
        return "Type,ID,Name,Time In,Time Out,Time Diff,Procedure,Station Name,State";
    }
}
