package com.olympus.simulation;

import java.io.Serializable;

public class ActorCSV implements Simulation_Manager.CSVable, Serializable {
    String actorType;
    String procedures;
    String id;

    ActorCSV() {
        actorType = "";
        procedures = "";
        id = "";
    }

    ActorCSV(String actorType, String procedures, String id) {
        this.actorType = actorType;
        this.procedures = procedures;
        this.id = id;
    }

    public void setActorType(String actorType) {
        this.actorType = actorType;
    }

    public void setProcedures(String procedures) {
        this.procedures = procedures;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getCSV() {
        return actorType + "," + procedures + "," + id;
    }

    @Override
    public String getCSVHeader() {
        return "Type,Procedures,Serial Number";
    }

    public interface Actor {
        ActorCSV getActorCSV();
    }
}
