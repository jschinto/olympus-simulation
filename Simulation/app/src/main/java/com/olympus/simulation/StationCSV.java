package com.olympus.simulation;

import java.io.Serializable;

public class StationCSV implements Serializable, Simulation_Manager.CSVable {
    String stationType;
    String serialNum;
    String name;

    StationCSV() {
        stationType = "";
        serialNum = "";
        name = "";
    }

    StationCSV(String stationType, String serialNum, String name) {
        this.stationType = stationType;
        this.serialNum = serialNum;
        this.name = name;
    }

    public void setStationType(String stationType) {
        this.stationType = stationType;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCSV() {
        return stationType + "," + serialNum + "," + name;
    }

    @Override
    public String getCSVHeader() {
        return "Type,Serial Number,Name";
    }

    public interface Station {
        StationCSV getStationCSV();
    }
}
