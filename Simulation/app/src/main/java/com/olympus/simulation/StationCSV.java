package com.olympus.simulation;

import java.io.Serializable;

public class StationCSV implements Serializable, Simulation_Manager.CSVable {
    String stationType;
    String name;

    StationCSV() {
        stationType = "";
        name = "";
    }

    StationCSV(String stationType, String name) {
        this.stationType = stationType;
        this.name = name;
    }

    public void setStationType(String stationType) {
        this.stationType = stationType;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCSV() {
        return stationType + "," + name;
    }

    @Override
    public String getCSVHeader() {
        return "Type,Serial Number";
    }

    public interface Station {
        StationCSV getStationCSV();
    }
}
