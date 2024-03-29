package com.olympus.simulation;

import java.io.Serializable;

public class EquipmentCSV implements Simulation_Manager.CSVable, Serializable {
    String equipType;
    String model;
    String serialNum;

    EquipmentCSV() {
        equipType = "";
        model = "";
        serialNum = "";
    }

    EquipmentCSV(String equipType, String model, String serialNum) {
        this.equipType = equipType;
        this.model = model;
        this.serialNum = serialNum;
    }

    public void setEquipType(String equipType) {
        this.equipType = equipType;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    @Override
    public String getCSV() {
        return equipType + "," + model + "," + serialNum + "," + equipType + " " + serialNum;
    }

    @Override
    public String getCSVHeader() {
        return "Type,Model,Serial Number,Name";
    }

    public interface Equipment {
        EquipmentCSV getEquipmentCSV();
    }
}
